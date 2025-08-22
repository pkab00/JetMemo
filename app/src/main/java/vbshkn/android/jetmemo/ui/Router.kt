package vbshkn.android.jetmemo.ui

import kotlinx.serialization.Serializable

sealed class Router{
    @Serializable
    object HomeRoute

    @Serializable
    data class UnitRoute(
        val id: Int,
        val name: String
    )

    @Serializable
    data class LearningRoute(
        val id: Int
    )

    @Serializable
    data class LearningEndRoute(
        val total: Int,
        val mistakes: Int,
        val timeString: String
    )
}

