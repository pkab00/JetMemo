package vbshkn.android.jetmemo.model

import android.content.Intent.ShortcutIconResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vbshkn.android.jetmemo.data.UnitEntity
import vbshkn.android.jetmemo.data.UnitRepository
import vbshkn.android.jetmemo.data.WordEntity

class UnitScreenModel(
    private val repository: UnitRepository,
    val unitID: Int
) : ViewModel() {
    var sortMode = mutableStateOf(SortMode.getNextMode())
        private set
    val unitWords = repository.getAllFromUnit(unitID).map { data ->
        when (sortMode.value) {
            is SortMode.ByOriginalAsc -> data.sortedBy { it.original }
            is SortMode.ByOriginalDesc -> data.sortedByDescending { it.original }
            is SortMode.ByTranslationAsc -> data.sortedBy { it.translation }
            is SortMode.ByTranslationDesc -> data.sortedByDescending { it.translation }
            is SortMode.ByTimeAsc -> data.sortedBy { it.createdAt }
            is SortMode.ByTimeDesc -> data.sortedByDescending { it.createdAt }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _unitsToAddTo = MutableStateFlow<List<UnitEntity>>(emptyList())
    val unitsToAddTo = _unitsToAddTo.asStateFlow()


    fun addWord(word: WordEntity) {
        viewModelScope.launch {
            repository.addWord(word, unitID)
        }
    }

    fun addWordToAnotherUnit(
        word: WordEntity,
        unitID: Int
    ) {
        viewModelScope.launch {
            repository.addWord(word, unitID)
        }
    }

    fun editWord(word: WordEntity) {
        viewModelScope.launch {
            repository.editWord(word)
        }
    }

    fun deleteWordFromUnit(word: WordEntity) {
        viewModelScope.launch {
            repository.deleteWordFromUnit(word, unitID)
        }
    }

    fun deleteWordCompletely(word: WordEntity) {
        viewModelScope.launch {
            repository.deleteWordCompletely(word)
        }
    }

    fun loadUnitsToAddTo(word: WordEntity) {
        viewModelScope.launch {
            _unitsToAddTo.value = repository.getUnitsToAddTo(word)
        }
    }

    var dialogState by mutableStateOf<DialogState>(DialogState.None)
        private set

    fun showDialog(state: DialogState) {
        dialogState = state
    }

    fun dismissDialog() {
        dialogState = DialogState.None
    }

    fun nextSortMode(){
        sortMode.value = SortMode.getNextMode()
    }

    sealed class DialogState {
        data object None : DialogState()
        data object AddWordDialog : DialogState()
        data class EditWordDialog(val word: WordEntity) : DialogState()
        data class DeleteWordDialog(val word: WordEntity) : DialogState()
        data class AddToAnotherUnitDialog(val word: WordEntity) : DialogState()
    }

    sealed class SortMode(
        nameRes: Long,
        iconRes: Long
    ) {
        data object ByOriginalAsc : SortMode(0, 0)
        data object ByOriginalDesc : SortMode(0, 0)
        data object ByTranslationAsc : SortMode(0, 0)
        data object ByTranslationDesc : SortMode(0, 0)
        data object ByTimeAsc : SortMode(0, 0)
        data object ByTimeDesc : SortMode(0, 0)

        companion object {
            private var currentIndex = 0
            private val modes = listOf(
                ByOriginalAsc, ByOriginalDesc,
                ByTranslationAsc, ByTranslationDesc,
                ByTimeAsc, ByTimeDesc
            )

            fun getNextMode(): SortMode {
                if (currentIndex == modes.size) {
                    currentIndex = 0
                }
                return modes[currentIndex++]
            }
        }
    }
}