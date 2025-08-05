package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vbshkn.android.jetmemo.data.HomeRepository

/**
 * Кастомная фабрика ViewModel, которая (в отличие от дефолтной) принимает
 * в качестве параметра нужный класс Repository.
 *
 * Реализация любезно предоставлена Qwen 3 Coder.
 */
class HomeScreenModelFactory(private val repository: HomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}