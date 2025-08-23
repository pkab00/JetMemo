package vbshkn.android.jetmemo.logic

/**
 * Интерфейс, используемый как callback для передачи польовательского ввода
 * при выполнении упражнения.
 * @see Exercise
 */
sealed interface Answer {
    /**
     * Класс, принимающий в качестве ответа пару строк.
     * @param str1 первая строка
     * @param str2 вторая строка
     */
    data class WordPair(val str1: String, val str2: String) : Answer
    /**
     * Класс, принимающий в качестве ответа значение boolean, соответствующее да/нет.
     * @param answer ответ пользвателя в формате boolean
     */
    data class YesNo(val answer: Boolean) : Answer
}

/**
 * Интерфейс упражнения, используемый тренажёром.
 * @see LearnWordsTrainer
 */
sealed interface Exercise {
    /**
     * Проверяет, закончено ли выполнение упражнения.
     * @return true - если упражнение выполнено, иначе - false
     */
    fun done(): Boolean

    /**
     * Проверка ответа пользователя на упражнение или один из его этапов.
     * @return true - если ответ верный, иначе - false
     */
    fun checkAnswer(answer: Answer): Boolean

    /**
     * Количество очков, начисляемое за выполнение упражнения.
     * Минимум - 0, максимум - 1.
     */
    val learningPoints: Float

    /**
     * Упражнение на выбор правильного ответа среди представленного списка.
     * Поддерживаются как упражнения на выбор перевода, так на выбор слова по его переводу.
     * Конкретный подтип упражнения определяется ViewModel.
     * @param options варианты ответа
     * @param correctAnswer правильный ответ
     */
    class CorrectOptionExercise(
        val options: List<Word>,
        val correctAnswer: Word,
    ) : Exercise {
        private var done = false

        /**
         * Количество очков, начисляемое за выполнение упражнения.
         * Минимум - 0, максимум - 1.
         */
        override val learningPoints: Float = 0.4f

        /**
         * Проверка ответа пользователя на упражнение или один из его этапов.
         * @return true - если ответ верный, иначе - false
         */
        override fun checkAnswer(answer: Answer): Boolean {
            if (answer is Answer.WordPair) {
                val (str1, str2) = answer
                done = (str1 == correctAnswer.original && str2 == correctAnswer.translation)
                        || (str1 == correctAnswer.translation && str2 == correctAnswer.original)
                return done
            }
            return false
        }

        /**
         * Проверяет, закончено ли выполнение упражнения.
         * @return true - если упражнение выполнено, иначе - false
         */
        override fun done(): Boolean {
            return done
        }
    }

    /**
     * Упражнение на сосоставление пар "слово-перевод".
     * Упражнение не считается выполненным, пока не собраны все пары.
     * @param options список используемых слов
     */
    class MatchPairsExercise(
        var options: MutableList<Word>,
    ) : Exercise {
        private val matched = mutableListOf<Word>()

        /**
         * Количество очков, начисляемое за выполнение упражнения.
         * Минимум - 0, максимум - 1.
         */
        override val learningPoints: Float = 0.3f

        /**
         * Проверка ответа пользователя на упражнение или один из его этапов.
         * @return true - если ответ верный, иначе - false
         */
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

        /**
         * Проверяет, закончено ли выполнение упражнения.
         * @return true - если упражнение выполнено, иначе - false
         */
        override fun done(): Boolean {
            return matched.containsAll(options)
        }
    }

    /**
     * Упражнение, в котором пользователь должен определить, корректно ли переведено слово на экране.
     * Слово, выводимое на экран, собирается случайным образом, и может не соответствовать ожидаемому.
     * @param givenWord выводимое на экран слово
     * @param correctWord ожидаемое, корректно переведённое слово
     */
    class ApproveTranslationExercise(
        val givenWord: Word,
        val correctWord: Word,
    ) : Exercise {
        private var done = false

        /**
         * Количество очков, начисляемое за выполнение упражнения.
         * Минимум - 0, максимум - 1.
         */
        override val learningPoints: Float = 0.2f

        /**
         * Проверка ответа пользователя на упражнение или один из его этапов.
         * @return true - если ответ верный, иначе - false
         */
        override fun checkAnswer(answer: Answer): Boolean {
            if (answer is Answer.YesNo) {
                done = (givenWord == correctWord) == answer.answer
                return done
            }
            return false
        }

        /**
         * Проверяет, закончено ли выполнение упражнения.
         * @return true - если упражнение выполнено, иначе - false
         */
        override fun done(): Boolean {
            return done
        }
    }

    /**
     * Тип-заглушка для случаев, когда текущее упражнение ещё не сгенерировано.
     */
    data object Unspecified : Exercise {
        /**
         * Количество очков, начисляемое за выполнение упражнения.
         * Минимум - 0, максимум - 1.
         */
        override val learningPoints: Float = 0f

        /**
         * Проверяет, закончено ли выполнение упражнения.
         * @return true - если упражнение выполнено, иначе - false
         */
        override fun done(): Boolean {
            return false
        }

        /**
         * Проверка ответа пользователя на упражнение или один из его этапов.
         * @return true - если ответ верный, иначе - false
         */
        override fun checkAnswer(answer: Answer): Boolean {
            return false
        }
    }
}