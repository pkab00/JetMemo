package vbshkn.android.jetmemo.model

import android.util.Log

data class Word(
    val original: String,
    val translation: String,
    var learned: Boolean = false
)

data class Question(
    val answers: List<Word>,
    val correctAnswer: Word
)

class LearnWordsTrainer{
    private final val TAG = this.javaClass.simpleName

    val dictionary: List<Word> = listOf(
        Word("Cat", "Кот"),
        Word("Dog", "Собака"),
        Word("Building", "Здание"),
        Word("Soup", "Суп"),
        Word("Balcony", "Балкон"),
        Word("Table", "Стол"),
        Word("Space", "Космос"),
        Word("Science", "Наука"),
        Word("Tears", "Слёзы"),
        Word("Magic", "Магия"),
        Word("Skill", "Навык"),
        Word("Language", "Язык"),
        Word("Teacher", "Учитель"),
        Word("Way", "Путь"),
        Word("Dream", "Мечта"),
        Word("Sense", "Чувство"),
        Word("Road", "Дорога"),
        Word("Love", "Любовь"),
        Word("Care", "Забота"),
        Word("Option", "Вариант"),
        Word("Network", "Сеть"),
        Word("Illness", "Болезнь"),
        Word("Hue", "Оттенок"),
        Word("Color", "Цвет"),
        Word("End", "Конец")
    )

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
        var questionSet = listOf<Word>()

        if(notLearned.isEmpty()){
            return null
        }

        questionSet =
            if(notLearned.size >= WORDS_NEEDED) notLearned.take(WORDS_NEEDED)
            else notLearned + learned.take(WORDS_NEEDED - notLearned.size)
            // TODO: сделать так, чтобы повторяющиеся слова не были правильными ответами
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