package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import vbshkn.android.jetmemo.data.LearnRepository

class LearnScreenModelFactory(
    private val repository: LearnRepository,
    private val navController: NavController,
    private val unitID: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LearnScreenModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LearnScreenModel(repository, navController, unitID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}