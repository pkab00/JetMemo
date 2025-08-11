package vbshkn.android.jetmemo.logic

import vbshkn.android.jetmemo.data.WordEntity

data class Word(
    val original: String,
    val translation: String,
    var learned: Boolean = false
) {
    constructor(entity: WordEntity) : this(
        original = entity.original,
        translation = entity.translation
    )
}

data class Question(
    val answers: List<Word>,
    val correctAnswer: Word
)

class LearnWordsTrainer(entities : List<WordEntity>){
    private val TAG = this.javaClass.simpleName

    var dictionary : List<Word> = entities.map { entity -> Word(entity) }

    private var currentQuestion: Question? = null

    private fun getLearnedWords(): List<Word>{
        return dictionary.filter { it.learned }.shuffled()
    }

    private fun getNotLearnedWords(): List<Word>{
        return  dictionary.filter { !it.learned }.shuffled()
    }

    fun generateNextQuestion(): Question?{
        val notLearned = getNotLearnedWords()
        val learned = getLearnedWords()
        val WORDS_NEEDED = 4;

        if(notLearned.isEmpty()){
            return null
        }

        var questionSet: List<Word> = if(notLearned.size >= WORDS_NEEDED) notLearned.take(WORDS_NEEDED)
            else notLearned + learned.take(WORDS_NEEDED - notLearned.size)
        currentQuestion = Question(questionSet, questionSet.random())
        return currentQuestion
    }

    fun checkAnswer(answerIndex: Int): Boolean {
        currentQuestion?.let {
            if(it.answers[answerIndex] == it.correctAnswer){
               dictionary[dictionary.indexOf(it.correctAnswer)].learned = true
                return true
            }
            else{
                return false
            }
        } ?: return false
    }
}