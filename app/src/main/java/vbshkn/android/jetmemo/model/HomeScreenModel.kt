package vbshkn.android.jetmemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.data.HomeRepository

/**
 * Модель для управления HomeScreen.
 * @param repository репозиторий Home
 */
class HomeScreenModel(private val repository: HomeRepository): ViewModel() {
    val allUnits: StateFlow<List<UnitEntity>> = repository.getUnits()
        // превращаем холодный поток (Flow) в горячий (StateFlow)
        .stateIn(
            scope = viewModelScope, // scope, в котором работает flow
            // прекращаем сбор данных если подписчиков нет более 5 сек.
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // значение по умолчанию
        )

    /**
     * Добавление нового юнита в список.
     * @param name название юнита
     */
    fun addUnit(name: String){
        viewModelScope.launch {
            repository.insertUnit(name)
        }
    }

    /**
     * Удаление юнита из списка.
     * @param unit объект юнита
     */
    fun deleteUnit(unit: UnitEntity){
        viewModelScope.launch {
            repository.deleteUnit(unit)
        }
    }

    /**
     * Изменение данных о юните.
     * @param unit обновлённый объект юнита
     */
    fun editUnit(unit: UnitEntity){
        viewModelScope.launch {
            repository.editUnit(unit)
        }
    }

    // текущее состояние DialogHost
    var dialogState by mutableStateOf<DialogState>(DialogState.None)
        private set

    /**
     * Изменение текущего состояния DialogState для отображения диалогового окна.
     * @param newState новое состояние
     */
    fun showDialog(newState: DialogState){
        dialogState = newState
    }

    /**
     * Сокрытие диалоговых окон путём установки DialogState по умолчанию.
     */
    fun dismissDialog(){
        dialogState = DialogState.None
    }

    /**
     * Набор возможных состояний для DialogHost.
     */
    sealed class DialogState{
        data object None: DialogState() // все окна скрыты (по умолчанию)
        data object AddUnitDialog: DialogState() // диалог добавления юнита
        data object AboutDialog: DialogState() // диалог "О приложении"
        data class EditUnitDialog(val unit: UnitEntity): DialogState() // диалог изменения юнита
        data class DeleteUnitDialog(val unit: UnitEntity): DialogState() // диалог удаления юнита
    }
}

/**
 * Режимы сортировки списка юнитов.
 */
enum class SortMode{
    NEW_TO_OLD, // по убыванию
    OLD_TO_NEW // по возрастанию
}