package vbshkn.android.jetmemo.logic

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import vbshkn.android.jetmemo.data.WordEntity
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KClass


class LearnWordsTrainer(entities: List<WordEntity>) {
    private var dictionary: List<Word> = entities.map { entity -> Word(entity) }
    private val wordsNeededMap = mapOf(
        Exercise.MatchPairsExercise::class to 4,
        Exercise.CorrectOptionExercise::class to 3,
        Exercise.ApproveTranslationExercise::class to 1
    )
    private val learnedStateMap = mutableMapOf<Word, Boolean>()
    private var _currentExercise = MutableStateFlow<Exercise>(Exercise.Unspecified)
    var currentExercise = _currentExercise.asStateFlow()

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
        if (filtered.isEmpty()) {
            _currentExercise.value = Exercise.Unspecified
        } else {
            val exerciseClass = filtered.keys.random()
            _currentExercise.value = buildExercise(exerciseClass)
        }
    }

    fun checkAnswer(answer: Answer): Boolean {
        return currentExercise.value.checkAnswer(answer)
    }

    fun isDone(): Boolean {
        return currentExercise.value.done()
    }

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