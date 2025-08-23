package vbshkn.android.jetmemo.ui

import kotlinx.serialization.Serializable

/**
 * Класс, используемый при осуществлении навигации между экранами приложения.
 * @see MainActivity
 */
sealed class Router{
    /**
     * Роут для навигации на HomeScreen.
     */
    @Serializable
    object HomeRoute

    /**
     * Роут для навигации на UnitScreen.
     * @param id идентификатор юнита
     * @param name название юнита
     */
    @Serializable
    data class UnitRoute(
        val id: Int,
        val name: String
    )

    /**
     * Роут для навигации на LearningScreen.
     * @param id идентификатор юнита
     */
    @Serializable
    data class LearningRoute(
        val id: Int
    )

    /**
     * Роут для навигации на LearningEndScreen.
     * @param total количество пройденных упражнений
     * @param mistakes количество допущенных ошибок
     * @param timeString затраченное время в виде строки MM:SS
     */
    @Serializable
    data class LearningEndRoute(
        val total: Int,
        val mistakes: Int,
        val timeString: String
    )
}

