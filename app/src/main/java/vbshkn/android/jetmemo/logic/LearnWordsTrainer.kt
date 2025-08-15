package vbshkn.android.jetmemo.logic

import vbshkn.android.jetmemo.data.WordEntity
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KClass


class LearnWordsTrainer(entities: List<WordEntity>) {
    private var dictionary: List<Word> = entities.map { entity -> Word(entity) }
    private val wordsNeededMap = mapOf(
        Exercise.MatchPairsExercise::class to 4,
        Exercise.RightOptionExercise::class to 3,
        Exercise.IsCorrectTranslationExercise::class to 1
    )
    private val statesNeededMap = mapOf(
        Exercise.MatchPairsExercise::class to 4,
        Exercise.RightOptionExercise::class to 3,
        Exercise.IsCorrectTranslationExercise::class to 2
    )
    private val learnedStateMap = mutableMapOf<Word, Boolean>()
    var currentExercise: Exercise = Exercise.Unspecified
        private set

    private fun getLearnedWords(): List<Word> {
        return dictionary.filter { isLearned(it) }.shuffled()
    }

    private fun getNotLearnedWords(): List<Word> {
        return dictionary.filter { !isLearned(it) }.shuffled()
    }

    private fun isLearned(word: Word): Boolean {
        return learnedStateMap[word] == true
    }

    fun setLearned(word: Word) {
        learnedStateMap[word] = true
    }

    fun generateNextExercise() {
        val notLearned = getNotLearnedWords()
        val filtered = wordsNeededMap.filter { it.value <= notLearned.size }
        val exerciseClass = filtered.keys.random()
        currentExercise = buildExercise(exerciseClass)
    }

    fun checkAnswer(answer: Answer): Boolean {
        return currentExercise.checkAnswer(answer)
    }

    fun getStatesNumber(): Int? {
        val klass = currentExercise::class
        return statesNeededMap[klass]
    }

    private fun buildExercise(clazz: KClass<out Exercise>): Exercise {
        val learned = getLearnedWords()
        val notLearned = getNotLearnedWords()

        when (clazz) {
            Exercise.RightOptionExercise::class -> {
                val randomWords = wordsNeededMap[clazz]?.let { notLearned.take(it) }
                if (randomWords != null) {
                    return Exercise.RightOptionExercise(randomWords, randomWords.random())
                }
            }

            Exercise.MatchPairsExercise::class -> {
                val randomWords = wordsNeededMap[clazz]?.let { notLearned.take(it) }
                if (randomWords != null) {
                    return Exercise.MatchPairsExercise(randomWords.toMutableList())
                }
            }

            Exercise.IsCorrectTranslationExercise::class -> {
                val randomNotLearned = notLearned.random()
                val randomLearned = if (learned.isEmpty()) notLearned.random() else learned.random()
                val coff = Random.nextInt(0..100)

                if (coff < 50) {
                    val wrongTranslatedWord = Word(
                        original = randomNotLearned.original,
                        translation = randomLearned.translation
                    )
                    return Exercise.IsCorrectTranslationExercise(
                        wrongTranslatedWord,
                        randomNotLearned
                    )
                } else return Exercise.IsCorrectTranslationExercise(
                    randomNotLearned,
                    randomNotLearned
                )
            }
        }
        throw IllegalStateException("Can not recognize given Exercise class")
    }
}