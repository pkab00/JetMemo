package vbshkn.android.jetmemo.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import vbshkn.android.jetmemo.data.LearningRepository
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.logic.LearnWordsTrainer
import vbshkn.android.jetmemo.model.sub.ApproveTranslationSubModel
import vbshkn.android.jetmemo.model.sub.LearningScreenSubModel
import vbshkn.android.jetmemo.model.sub.MatchPairsSubModel
import vbshkn.android.jetmemo.model.sub.CorrectOptionSubModel
import vbshkn.android.jetmemo.ui.Router


/**
 * Модель для управления LearningScreen.
 * @param repository репозиторий Learning
 * @param navController объект для навигации между экранами
 * @param unitID идентификатор юнита

 */
class LearningScreenModel(
    repository: LearningRepository,
    private val navController: NavController,
    val unitID: Int
) : ViewModel() {
    // тип упражнения <=> кол-во необходимых объектов состояния для UI
    private val statesNeededMap = mapOf(
        Exercise.MatchPairsExercise::class to 8,
        Exercise.CorrectOptionExercise::class to 3,
        Exercise.ApproveTranslationExercise::class to 2
    )
    private var _canMoveFurther = false // флаг возможности перехода к следующему упражнению
    val canMoveFurther get() = _canMoveFurther
    private val words = repository.getWordsInUnit(unitID)
    private val trainer: LearnWordsTrainer = LearnWordsTrainer(words)
    val currentExercise = trainer.currentExercise // текущее упражнение

    // состояния элементов интерфейса
    private var _elementStates = MutableStateFlow<List<ElementState>>(emptyList())
    val elementStates = _elementStates.asStateFlow()

    // флаги видимости компонентов UI
    private val _bottomBarState = mutableStateOf(false)
    var bottomBarState by _bottomBarState
    private val _showBottomBar = mutableStateOf(false)
    var showBottomBar by _showBottomBar
    private val _showSkipButton = mutableStateOf(true)
    var showSkipButton by _showSkipButton

    // саб-модель для управления UI текущего упражнения
    private var _currentSubModel: LearningScreenSubModel? = null
    val currentSubModel get() = getSubModel()
    // объект, хранящий статистику прохождения
    private var statistics = Statistics()

    init {
        nextExercise()
    }

    /**
     * Навигация на главный экран.
     */
    fun exit() {
        navController.popBackStack()
    }

    /**
     * Навигация к финальному экрану со статистикой прохождения.
     */
    fun toEndScreen() {
        statistics = statistics.withTimerStopped()
        Log.d("DEBUG", "TIME: ${statistics.getTimeAsString()}")
        navController.popBackStack()
        navController.navigate(
            Router.LearningEndRoute(
                total = statistics.total,
                mistakes = statistics.mistakes,
                timeString = statistics.getTimeAsString()
            )
        )
    }

    /**
     * Получение состояния определённого элемента по его индексу.
     * @param index индекс элемента
     * @return объект ElementState либо null, если индекс недопустим
     */
    fun stateAt(index: Int): ElementState? {
        if (index >= 0 && index < elementStates.value.size) {
            return elementStates.value[index]
        }
        return null
    }

    /**
     * Задание нового состояния для элемента по указанному индексу.
     * @param index индекс элемента
     * @param color новый цвет
     * @param clickable кликабельный или нет
     */
    fun updateStateAt(index: Int, color: Color, clickable: Boolean) {
        if (index >= 0 && index < elementStates.value.size) {
            _elementStates.value = _elementStates.value.toMutableStateList().apply {
                this[index] = this[index].copy(color, clickable)
            }
        }
    }

    /**
     * @return количество состояний, необходимое для элементов UI
     * текущего подтипа Exercise
     */
    private fun getStatesNumber(): Int? {
        val klass = currentExercise.value::class
        return statesNeededMap[klass]
    }

    /**
     * Очистка массива состояний элементов и заполнение его нужным количеством
     * какого-то дефолтного подтипа ElementState.
     * @param defaultState состояние по умолчанию
     */
    fun resetAllStates(
        defaultState: ElementState
    ) {
        val newList = mutableListOf<ElementState>()
        getStatesNumber()?.let {
            repeat(it) {
                newList.add(defaultState)
            }
        }
        _elementStates.value = newList
    }

    /**
     * Вариация метода, делигирующая очистку списка состояний саб-модели,
     * которая сама определяет нужный тип состояния.
     * @see LearningScreenSubModel.resetAllStates
     */
    private fun resetAllStates() {
        currentSubModel?.resetAllStates() ?: resetAllStates(ElementState())
    }

    /**
     * Скрывает кнопку пропуска упражнения и отображает нижнюю панель с результатом.
     * @param correct флаг состояния нижней панели
     */
    fun showBottomBar(correct: Boolean) {
        bottomBarState = correct
        showBottomBar = true
        showSkipButton = false
    }

    /**
     * Скрывает нижнюю панель.
     */
    fun hideBottomBar() {
        showBottomBar = false
        showSkipButton = true
    }


    /**
     * Переход к следующему упражнению.
     * Генерирует само упражнение, обновляет список состояний и саб-модель.
     */
    fun nextExercise() {
        trainer.generateNextExercise()
        statistics = statistics.withExerciseCounted()
        resetAllStates()
        resetSubModel()
    }

    /**
     * Проверка пользовательского ответа.
     * Помимо непосредственно проверки, также определяет, завершено ли выполнение упражнения,
     * обновляет статистику и начисляет очки изучения.
     * @param answer ответ пользователя
     * @return true если ответ верный, иначе false
     */
    fun checkAnswer(answer: Answer): Boolean {
        val correct = trainer.checkAnswer(answer)
        val done = trainer.isDone()

        if (!correct) statistics = statistics.withMistakeCounted()
        when (val ex = currentExercise.value) {
            is Exercise.MatchPairsExercise -> {
                if (correct && done) ex.options.forEach { trainer.addLearningPoints(it) }
                _canMoveFurther = done
            }

            is Exercise.ApproveTranslationExercise -> {
                if (correct && done) trainer.addLearningPoints(ex.correctWord)
                _canMoveFurther = true
            }

            is Exercise.CorrectOptionExercise -> {
                if (correct && done) trainer.addLearningPoints(ex.correctAnswer)
                _canMoveFurther = true
            }

            else -> {}
        }
        return correct
    }

    /**
     * @return true если выполнение текущего упражнения завершено, иначе false
     */
    fun isDone(): Boolean {
        return trainer.currentExercise.value.done()
    }

    /**
     * @return саб-модель, соответствующая типу текущего упражнения
     */
    private fun getSubModel(): LearningScreenSubModel? {
        when (currentExercise.value) {
            is Exercise.CorrectOptionExercise -> {
                if (_currentSubModel !is CorrectOptionSubModel) {
                    _currentSubModel = CorrectOptionSubModel(this)
                }
                return _currentSubModel as CorrectOptionSubModel
            }

            is Exercise.MatchPairsExercise -> {
                if (_currentSubModel !is MatchPairsSubModel) {
                    _currentSubModel = MatchPairsSubModel(this)
                }
                return _currentSubModel as MatchPairsSubModel
            }

            is Exercise.ApproveTranslationExercise -> {
                if (_currentSubModel !is ApproveTranslationSubModel) {
                    _currentSubModel = ApproveTranslationSubModel(this)
                }
                return _currentSubModel as ApproveTranslationSubModel
            }

            else -> return null
        }
    }

    /**
     * Сброс текущей саб-модели.
     */
    private fun resetSubModel() {
        _currentSubModel = null
    }

    /**
     * Класс для хранения состояния элемента UI.
     * @param color основной цвет
     * @param clickable кликабельность
     */
    data class ElementState(
        var color: Color = Color.Unspecified,
        var clickable: Boolean = true
    )

    /**
     * Класс, содержащий статистику текущего прохождения.
     * Является полностью иммутабельным, так как вместо мутации полей
     * создаёт новую инстанцию типа.
     * @param total количество сгенерированных упражнений
     * @param mistakes количество допущенных ошибок
     * @param startTimestamp время начала тренеровки
     * @param endTimestamp время окончания тренеровки
     */
    data class Statistics(
        val total: Int = 0,
        val mistakes: Int = 0,
        private val startTimestamp: Long = System.currentTimeMillis(),
        private val endTimestamp: Long = 0L
    ) {
        /**
         * @return новая инстанция с зафиксированным временем окончания тренеровки
         */
        fun withTimerStopped(): Statistics {
            return if (endTimestamp == 0L) {
                copy(endTimestamp = System.currentTimeMillis())
            } else this
        }

        /**
         * @return новая инстанция с +1 подсчитанным упражнением
         */
        fun withExerciseCounted(): Statistics {
            return copy(total = total + 1)
        }

        /**
         * @return новая инстанция с +1 подсчитанной ошибкой
         */
        fun withMistakeCounted(): Statistics {
            return copy(mistakes = mistakes + 1)
        }

        /**
         * @return время прохождения тренеровки в виде строки формата MM:SS
         */
        fun getTimeAsString(): String {
            val seconds = (endTimestamp - startTimestamp) / 1000
            val minutes = seconds / 60
            val secondsLeft = seconds % 60
            val  mString = if(minutes < 10) "0$minutes" else minutes.toString()
            val sString = if(secondsLeft < 10) "0$secondsLeft" else secondsLeft.toString()
            return "$mString:$sString"
        }
    }
}