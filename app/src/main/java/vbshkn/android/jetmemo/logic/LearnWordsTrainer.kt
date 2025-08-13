package vbshkn.android.jetmemo.logic

import vbshkn.android.jetmemo.data.WordEntity
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KClass


class LearnWordsTrainer(entities: List<WordEntity>) {
    private var dictionary: List<Word> = entities.map { entity -> Word(entity) }
    private val wordsNeededMap = mapOf(
        Exercise.MatchPairsQuestion::class to 4,
        Exercise.RightOptionQuestion::class to 3,
        Exercise.IsCorrectTranslationQuestion::class to 1
    )
    var currentExercise: Exercise? = null
        private set

    private fun getLearnedWords(): List<Word> {
        return dictionary.filter { it.learned }.shuffled()
    }

    private fun getNotLearnedWords(): List<Word> {
        return dictionary.filter { !it.learned }.shuffled()
    }

    fun generateNextExercise(): Exercise? {
        val notLearned = getNotLearnedWords()

        if (notLearned.isEmpty()) {
            return null
        }
        var _randomIndex = Random.nextInt(0..wordsNeededMap.size)
        val randomIndex: Int = _randomIndex
        while (wordsNeededMap.values.toList()[randomIndex] > notLearned.size) {
            _randomIndex = Random.nextInt(0..wordsNeededMap.size)
        }
        val exerciseClass = wordsNeededMap.keys.toList()[randomIndex]
        return buildExercise(exerciseClass)
    }

    fun checkAnswer(answer: Answer): Boolean {
        return currentExercise?.checkAnswer(answer) ?: false
    }

    private fun buildExercise(clazz: KClass<out Exercise>): Exercise? {
        val learned = getLearnedWords()
        val notLearned = getNotLearnedWords()

        when (clazz) {
            Exercise.RightOptionQuestion::class -> {
                val randomWords = wordsNeededMap[clazz]?.let { notLearned.take(it) }
                if (randomWords != null) {
                    return Exercise.RightOptionQuestion(randomWords, randomWords.random())
                }
            }

            Exercise.MatchPairsQuestion::class -> {
                val randomWords = wordsNeededMap[clazz]?.let { notLearned.take(it) }
                if (randomWords != null) {
                    return Exercise.MatchPairsQuestion(randomWords.toMutableList())
                }
            }

            Exercise.IsCorrectTranslationQuestion::class -> {
                val randomNotLearned = notLearned.random()
                val randomLearned = if (learned.isEmpty()) notLearned.random() else learned.random()
                val coff = Random.nextInt(0..100)

                if (coff < 50) {
                    val wrongTranslatedWord = Word(
                        original = randomNotLearned.original,
                        translation = randomLearned.translation
                    )
                    return Exercise.IsCorrectTranslationQuestion(
                        wrongTranslatedWord,
                        randomNotLearned
                    )
                } else return Exercise.IsCorrectTranslationQuestion(
                    randomNotLearned,
                    randomNotLearned
                )
            }
        }
        return null
    }
}