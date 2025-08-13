package vbshkn.android.jetmemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vbshkn.android.jetmemo.data.LearnRepository
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.LearnWordsTrainer

class LearnScreenModel(
    repository: LearnRepository,
    unitID: Int
) : ViewModel() {
    private val words = repository.getWordsInUnit(unitID)
    private val trainer: LearnWordsTrainer = LearnWordsTrainer(words)

    private val _currentExercise = mutableStateOf(trainer.generateNextExercise())
    var currentExercise by _currentExercise
    private var elementStates = mutableStateListOf<ElementState>()
    private val _bottomPanelState = mutableStateOf(false)
    var bottomPanelState by _bottomPanelState

    fun resetAllStates(capacity: Int) {
        elementStates.clear()
        repeat(capacity) {
            elementStates.add(ElementState.Neutral)
        }
    }

    fun setElementState(at: Int, state: ElementState){
        if(at < elementStates.size) {
            elementStates[at] = state
        }
    }

    fun nextExercise() {
        _currentExercise.value = trainer.generateNextExercise()
    }

    fun checkAnswer(answer: Answer): Boolean{
        return trainer.checkAnswer(answer)
    }

    fun isDone(): Boolean {
        return trainer.currentExercise?.done() ?: false
    }

    sealed class ElementState {
        data object Neutral : ElementState()
        data object Correct : ElementState()
        data object Wrong : ElementState()
    }
}