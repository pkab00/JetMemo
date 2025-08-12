package vbshkn.android.jetmemo.logic

sealed interface Exercise {
    fun done(): Boolean

    class FourOptionsQuestion(
        val options: List<Word>,
        val correctAnswer: Word
    ) : Exercise {
        private var done = false

        fun checkAnswer(str1: String, str2: String): Boolean {
            done = (str1 == correctAnswer.original && str2 == correctAnswer.translation)
                    || (str1 == correctAnswer.translation && str2 == correctAnswer.original)
            return done
        }

        override fun done(): Boolean {
            return done
        }
    }

    class MatchPairsQuestion(
        var options: MutableList<Word>,
    ) : Exercise {
        val matched = mutableListOf<Word>()

        fun checkAnswer(str1: String, str2: String): Boolean {
            val variants = listOf(Word(str1, str2), Word(str2, str1))
            variants.forEach { variant ->
                if (options.contains(variant)) {
                    matched.add(variant)
                    return true
                }
            }
            return false
        }

        override fun done(): Boolean{
            return matched.containsAll(options)
        }
    }

    class IsCorrectTranslationQuestion(
        val givenWord: Word,
        val correctWord: Word
    ) : Exercise {
        var done = false

        fun checkAnswer(answer: Boolean): Boolean {
            done = (givenWord == correctWord) == answer
            return done
        }

        override fun done(): Boolean {
            return done
        }
    }
}