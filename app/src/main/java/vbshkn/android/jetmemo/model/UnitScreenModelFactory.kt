package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vbshkn.android.jetmemo.data.UnitRepository

class UnitScreenModelFactory(
    private val repository: UnitRepository,
    private val unitID: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UnitScreenModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return UnitScreenModel(repository, unitID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}