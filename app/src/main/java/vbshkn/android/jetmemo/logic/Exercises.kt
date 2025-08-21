package vbshkn.android.jetmemo.logic

sealed interface Answer {
    data class WordPair(val str1: String, val str2: String) : Answer
    data class YesNo(val answer: Boolean) : Answer
}

sealed interface Exercise {
    fun done(): Boolean
    fun checkAnswer(answer: Answer): Boolean

    class CorrectOptionExercise(
        val options: List<Word>,
        val correctAnswer: Word
    ) : Exercise {
        private var done = false

        override fun checkAnswer(answer: Answer): Boolean {
            if (answer is Answer.WordPair) {
                val (str1, str2) = answer
                done = (str1 == correctAnswer.original && str2 == correctAnswer.translation)
                        || (str1 == correctAnswer.translation && str2 == correctAnswer.original)
                return done
            }
            return false
        }

        override fun done(): Boolean {
            return done
        }
    }

    class MatchPairsExercise(
        var options: MutableList<Word>,
    ) : Exercise {
        val matched = mutableListOf<Word>()

        override fun checkAnswer(answer: Answer): Boolean {
            if (answer is Answer.WordPair) {
                val (str1, str2) = answer
                val variants = listOf(Word(str1, str2), Word(str2, str1))
                variants.forEach { variant ->
                    if (options.contains(variant)) {
                        matched.add(variant)
                        return true
                    }
                }
                return false
            }
            return false
        }

        override fun done(): Boolean {
            return matched.containsAll(options)
        }
    }

    class ApproveTranslationExercise(
        val givenWord: Word,
        val correctWord: Word
    ) : Exercise {
        var done = false

        override fun checkAnswer(answer: Answer): Boolean {
            if (answer is Answer.YesNo) {
                done = (givenWord == correctWord) == answer.answer
                return done
            }
            return false
        }

        override fun done(): Boolean {
            return done
        }
    }

    data object Unspecified : Exercise {
        override fun done(): Boolean {
            return false
        }

        override fun checkAnswer(answer: Answer): Boolean {
            return false
        }
    }
}