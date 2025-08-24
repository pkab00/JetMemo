package vbshkn.android.jetmemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.data.UnitRepository
import vbshkn.android.jetmemo.data.WordEntity

/**
 * Модель для управления UnitScreen.
 * @param repository репозиторий Unit
 * @param unitID идентификатор юнита
 */
class UnitScreenModel(
    private val repository: UnitRepository,
    val unitID: Int
) : ViewModel() {
    private val _sortMode = MutableStateFlow<SortMode>(SortMode.ByTimeAsc)
    val sortMode = _sortMode.asStateFlow()

    val unitWords = combine(
        repository.getAllFromUnit(unitID), // комбинируем flow данных и flow режима сортировки
        sortMode // новый поток будет эммититься при изменении хотя бы одного из них
    ) { data, mode ->
        data.sortedByMode(mode)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _unitsToAddTo = MutableStateFlow<List<UnitEntity>>(emptyList())
    val unitsToAddTo = _unitsToAddTo.asStateFlow()

    /**
     * Добавление слова в юнит.
     * @param word объект слова
     */
    fun addWord(word: WordEntity) {
        viewModelScope.launch {
            repository.addWord(word, unitID)
        }
    }

    /**
     * Добавление слова в один из юнитов, доступных для этой операции.
     * @param word объект слова
     * @param unitID идентификатор юнита
     */
    fun addWordToAnotherUnit(
        word: WordEntity,
        unitID: Int
    ) {
        viewModelScope.launch {
            repository.addWord(word, unitID)
        }
    }

    /**
     * Изменение информации о слове.
     * @param word объект слова
     */
    fun editWord(word: WordEntity) {
        viewModelScope.launch {
            repository.editWord(word)
        }
    }

    /**
     * Удаление слова из данного юнита.
     * @param word объект слова
     */
    fun deleteWordFromUnit(word: WordEntity) {
        viewModelScope.launch {
            repository.deleteWordFromUnit(word, unitID)
        }
    }

    /**
     * Полное удаление слова из базы данных.
     * @param word объект слова
     */
    fun deleteWordCompletely(word: WordEntity) {
        viewModelScope.launch {
            repository.deleteWordCompletely(word)
        }
    }

    /**
     * Запрос из БД списка юнитов, в которые можно добавить данное слово.
     * @param word объект слова
     */
    fun loadUnitsToAddTo(word: WordEntity) {
        viewModelScope.launch {
            _unitsToAddTo.value = repository.getUnitsToAddTo(word)
        }
    }

    // текущее состояние DialogHost
    var dialogState by mutableStateOf<DialogState>(DialogState.None)
        private set

    /**
     * Изменение текущего состояния DialogState для отображения диалогового окна.
     * @param state новое состояние
     */
    fun showDialog(state: DialogState) {
        dialogState = state
    }

    /**
     * Сокрытие диалоговых окон путём установки DialogState по умолчанию.
     */
    fun dismissDialog() {
        dialogState = DialogState.None
    }

    /**
     * Смена режима сортировки слов в юните.
     * @param newMode новый режим сортировки SortMode
     */
    fun setSortMode(newMode: SortMode) {
        viewModelScope.launch {
            _sortMode.value = newMode
        }
    }

    /**
     * Функция-расширения для стандартного интерфейса List, позволяющая
     * сортировать списки с использованием стратегий сортировки SortMode.
     * @param mode режим сортировки SortMode
     * @see SortMode
     */
    private fun List<WordEntity>.sortedByMode(mode: SortMode): List<WordEntity> {
        return when (mode) {
            is SortMode.ByOriginalAsc -> sortedBy { it.original }
            is SortMode.ByTranslationAsc -> sortedBy { it.translation }
            is SortMode.ByTimeAsc -> sortedBy { it.createdAt }
        }
    }

    /**
     * Набор возможных состояний для DialogHost.
     */
    sealed class DialogState {
        data object None : DialogState() // окна скрыты
        data object AddWordDialog : DialogState() // окно добавления слова
        data class EditWordDialog(val word: WordEntity) : DialogState() // окно изменения слова
        data class DeleteWordDialog(val word: WordEntity) : DialogState() // окно удаления слова
        data class AddToAnotherUnitDialog(val word: WordEntity) : DialogState() // окно выбора юнитов для добавления
    }

    /**
     * Режимы сортировки слов в юните.
     * Все режимы сортируют слова по убыванию.
     */
    sealed class SortMode {
        data object ByOriginalAsc : SortMode() // по оригинальному слову
        data object ByTranslationAsc : SortMode() // по переводу
        data object ByTimeAsc : SortMode() // по времени создания
    }
}