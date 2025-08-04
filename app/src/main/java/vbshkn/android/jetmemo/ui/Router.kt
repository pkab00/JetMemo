package vbshkn.android.jetmemo.ui

import kotlinx.serialization.Serializable

sealed class Router{
    @Serializable
    object HomeRoute

    @Serializable
    data class UnitRoute(val id: Int)
}

