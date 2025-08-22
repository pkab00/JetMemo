package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class LearningEndScreenModel
    (
    private val navController: NavController,
    private val total: Int,
    private val mistakes: Int,
    private val timeString: String
) : ViewModel() {
    fun exit() {
        navController.popBackStack()
    }
}