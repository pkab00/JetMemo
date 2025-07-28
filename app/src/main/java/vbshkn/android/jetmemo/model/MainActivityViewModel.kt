package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.data.UnitRepository

class MainActivityViewModel(private val repository: UnitRepository): ViewModel() {
    val allUnits: StateFlow<List<UnitEntity>> = repository.getUnits()
        // превращаем холодный поток (Flow) в горячий (StateFlow)
        .stateIn(
            scope = viewModelScope, // scope, в котором работает flow
            // прекращаем сбор данных если подписчиков нет более 5 сек.
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // значение по умолчанию
        )

    fun addUnit(name: String){
        viewModelScope.launch {
            repository.insertUnit(name)
        }
    }

    fun deleteUnit(unit: UnitEntity){
        viewModelScope.launch {
            repository.deleteUnit(unit)
        }
    }
}