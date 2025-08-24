package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import vbshkn.android.jetmemo.data.LearningRepository

/**
 * Фабрика LearningScreenModel.
 * @param repository репозиторий Learning
 * @param navController объект для навигации между экранами
 * @param unitID идентификатор юнита
 * @throws IllegalArgumentException
 * @see LearningScreenModel
 */
class LearningScreenModelFactory(
    private val repository: LearningRepository,
    private val navController: NavController,
    private val unitID: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LearningScreenModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LearningScreenModel(repository, navController, unitID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}