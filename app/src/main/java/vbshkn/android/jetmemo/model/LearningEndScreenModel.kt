package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class LearningEndScreenModel(private val navController: NavController)
    : ViewModel() {
    fun exit() {
        navController.popBackStack()
    }
}