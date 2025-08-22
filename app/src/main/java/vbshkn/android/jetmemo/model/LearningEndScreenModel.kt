package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class LearningEndScreenModel
    (
    private val navController: NavController,
    val total: Int,
    val mistakes: Int,
    val timeString: String
) : ViewModel() {
    fun exit() {
        navController.popBackStack()
    }
}