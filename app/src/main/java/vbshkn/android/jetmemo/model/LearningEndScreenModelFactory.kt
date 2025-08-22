package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import vbshkn.android.jetmemo.data.LearnRepository

class LearningEndScreenModelFactory(
    private val navController: NavController,
    private val total: Int,
    private val mistakes: Int,
    private val timeString: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LearningEndScreenModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LearningEndScreenModel(navController, total, mistakes, timeString) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}