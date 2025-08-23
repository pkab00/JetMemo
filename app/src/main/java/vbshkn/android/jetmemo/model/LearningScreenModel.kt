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

class LearningScreenModel(
    repository: LearningRepository, val navController: NavController, val unitID: Int
) : ViewModel() {
    private val statesNeededMap = mapOf(
        Exercise.MatchPairsExercise::class to 8,
        Exercise.CorrectOptionExercise::class to 3,
        Exercise.ApproveTranslationExercise::class to 2
    )
    private var _canMoveFurther = false
    val canMoveFurther get() = _canMoveFurther
    private val words = repository.getWordsInUnit(unitID)
    private val trainer: LearnWordsTrainer = LearnWordsTrainer(words)
    val currentExercise = trainer.currentExercise

    private var _elementStates = MutableStateFlow<List<ElementState>>(emptyList())
    val elementStates = _elementStates.asStateFlow()

    private val _bottomBarState = mutableStateOf(false)
    var bottomBarState by _bottomBarState
    private val _showBottomBar = mutableStateOf(false)
    var showBottomBar by _showBottomBar
    private val _showSkipButton = mutableStateOf(true)
    var showSkipButton by _showSkipButton

    private var _currentSubModel: LearningScreenSubModel? = null
    val currentSubModel get() = getSubModel()
    private var statistics = Statistics()

    init {
        nextExercise()
    }

    fun exit() {
        navController.popBackStack()
    }

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

    fun stateAt(index: Int): ElementState? {
        if (index >= 0 && index < elementStates.value.size) {
            return elementStates.value[index]
        }
        return null
    }

    fun updateStateAt(index: Int, color: Color, clickable: Boolean) {
        if (index >= 0 && index < elementStates.value.size) {
            _elementStates.value = _elementStates.value.toMutableStateList().apply {
                this[index] = this[index].copy(color, clickable)
            }
        }
    }

    private fun getStatesNumber(): Int? {
        val klass = currentExercise.value::class
        return statesNeededMap[klass]
    }

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

    private fun resetAllStates() {
        currentSubModel?.resetAllStates() ?: resetAllStates(ElementState())
    }

    fun showBottomBar(correct: Boolean) {
        bottomBarState = correct
        showBottomBar = true
        showSkipButton = false
    }

    fun hideBottomBar() {
        showBottomBar = false
        showSkipButton = true
    }


    fun nextExercise() {
        trainer.generateNextExercise()
        statistics = statistics.withExerciseCounted()
        resetAllStates()
        resetSubModel()
    }

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

    fun isDone(): Boolean {
        return trainer.currentExercise.value.done()
    }

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

    private fun resetSubModel() {
        _currentSubModel = null
    }

    data class ElementState(
        var color: Color = Color.Unspecified, var clickable: Boolean = true
    )

    data class Statistics(
        val total: Int = 0,
        val mistakes: Int = 0,
        val startTimestamp: Long = System.currentTimeMillis(),
        val endTimestamp: Long = 0L
    ) {
        fun withTimerStopped(): Statistics {
            return if (endTimestamp == 0L) {
                copy(endTimestamp = System.currentTimeMillis())
            } else this
        }

        fun withExerciseCounted(): Statistics {
            return copy(total = total + 1)
        }

        fun withMistakeCounted(): Statistics {
            return copy(mistakes = mistakes + 1)
        }

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