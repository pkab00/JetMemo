package vbshkn.android.jetmemo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vbshkn.android.jetmemo.data.UnitRepository
import vbshkn.android.jetmemo.data.WordEntity

class UnitScreenModel(
    private val repository: UnitRepository,
    private val unitID: Int
) : ViewModel() {
    val unitWords = repository.getAllFromUnit(unitID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addWord(word: WordEntity){
        viewModelScope.launch {
            repository.addWord(word, unitID)
        }
    }

    fun editWord(word: WordEntity){
        viewModelScope.launch {
            repository.editWord(word)
        }
    }

    fun deleteWordFromUnit(word: WordEntity){
        viewModelScope.launch {
            repository.deleteWordFromUnit(word, unitID)
        }
    }

    fun deleteWordCompletely(word: WordEntity){
        viewModelScope.launch {
            repository.deleteWordCompletely(word)
        }
    }
}