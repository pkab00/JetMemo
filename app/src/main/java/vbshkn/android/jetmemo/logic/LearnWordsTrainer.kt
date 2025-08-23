package vbshkn.android.jetmemo.logic

import android.util.Log
import androidx.collection.FloatList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import vbshkn.android.jetmemo.data.WordEntity
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KClass

/**
 * Класс, отвечающий для генерации упражнений в режиме заучивания.
 * Хранит текущее упражнение и обеспечивает генерацию последующих.
 */
class LearnWordsTrainer(entities: List<WordEntity>) {
    private var dictionary: List<Word> = entities.map { entity -> Word(entity) }
    // мапа хранит количество слов, необходимое для каждого типа упражнений
    private val wordsNeededMap = mapOf(
        Exercise.MatchPairsExercise::class to 4,
        Exercise.CorrectOptionExercise::class to 3,
        Exercise.ApproveTranslationExercise::class to 1
    )
    // мапа, где значение соответствует коэффициенту изучения слова
    private val learnedStateMap = dictionary.associateWith { 0f }.toMutableMap()
    private var _currentExercise = MutableStateFlow<Exercise>(Exercise.Unspecified)
    var currentExercise = _currentExercise.asStateFlow()

    /**
     * @return перемешанный список изученных слов
     */
    private fun getLearnedWords(): List<Word> {
        return dictionary.filter { isLearned(it) }.shuffled()
    }

    /**
     * @return перемешанный список неизученных слов
     */
    private fun getNotLearnedWords(): List<Word> {
        return dictionary.filter { !isLearned(it) }.shuffled()
    }

    /**
     * Проверяет, изучено ли слово.
     * Слово считается изученным, если коэффициент изучения больше или равен 1.
     * @param word объект слова
     * @return true если слово изучено, иначе - false
     */
    private fun isLearned(word: Word): Boolean {
        return learnedStateMap[word]!! >= 1
    }

    /**
     * Увеличивает коэффициент изучения слова значение, соответствующее типу упражнения.
     * @param word объект слова
     */
    fun addLearningPoints(word: Word) {
        learnedStateMap[word] =
            (learnedStateMap[word]?.plus(currentExercise.value.learningPoints) ?: return)
    }

    /**
     * Генерирует следующее упражнение.
     * Тип упражнения выбирается рандомно в зависимости от количества неизученных слов.
     * Если доступных типов нет, присваивается Exercise.Unspecified
     */
    fun generateNextExercise() {
        val notLearned = getNotLearnedWords()
        val filtered = wordsNeededMap.filter { it.value <= notLearned.size }
        if (filtered.isEmpty()) {
            _currentExercise.value = Exercise.Unspecified
        } else {
            val exerciseClass = filtered.keys.random()
            _currentExercise.value = buildExercise(exerciseClass)
        }
    }

    /**
     * Проверяет ответ пользователя.
     * @param answer ответ
     * @return true если ответ верный, иначе false
     */
    fun checkAnswer(answer: Answer): Boolean {
        return currentExercise.value.checkAnswer(answer)
    }

    /**
     * Проверяет, завершено ли выполнение упражнения.
     * @return true если упражнение выполнено, иначе false
     * @throws IllegalStateException если передан некорректный класс
     */
    fun isDone(): Boolean {
        return currentExercise.value.done()
    }

    /**
     * Фабричный метод, отвечающий за создание упражнения выбранного типа.
     * @param clazz Kotlin-класс упражнения, реализующий интерфейс Exercise
     * @return готовое упражнение
     */
    private fun buildExercise(clazz: KClass<out Exercise>): Exercise {
        val learned = getLearnedWords()
        val notLearned = getNotLearnedWords()

        when (clazz) {
            Exercise.CorrectOptionExercise::class -> {
                val randomWords = wordsNeededMap[clazz]?.let { notLearned.take(it) }
                if (randomWords != null) {
                    Log.d("DEBUG", "RightOptionExercise built")
                    return Exercise.CorrectOptionExercise(randomWords, randomWords.random())
                }
            }

            Exercise.MatchPairsExercise::class -> {
                val randomWords = wordsNeededMap[clazz]?.let { notLearned.take(it) }
                if (randomWords != null) {
                    Log.d("DEBUG", "MatchPairsExercise built")
                    return Exercise.MatchPairsExercise(randomWords.toMutableList())
                }
            }

            Exercise.ApproveTranslationExercise::class -> {
                val randomNotLearned = notLearned.random()
                val randomLearned = if (learned.isEmpty()) notLearned.random() else learned.random()
                val coff = Random.nextInt(0..100)

                if (coff < 50) {
                    val wrongTranslatedWord = Word(
                        original = randomNotLearned.original,
                        translation = randomLearned.translation
                    )
                    Log.d("DEBUG", "ApproveTranslationExercise built")
                    return Exercise.ApproveTranslationExercise(
                        wrongTranslatedWord,
                        randomNotLearned
                    )
                } else {
                    Log.d("DEBUG", "ApproveTranslationExercise built")
                    return Exercise.ApproveTranslationExercise(
                        randomNotLearned,
                        randomNotLearned
                    )
                }
            }
        }
        throw IllegalStateException("Can not recognize given Exercise class")
    }
}