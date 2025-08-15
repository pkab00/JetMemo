package vbshkn.android.jetmemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import vbshkn.android.jetmemo.data.LearnRepository
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.logic.LearnWordsTrainer
import vbshkn.android.jetmemo.logic.Word

class LearnScreenModel(
    repository: LearnRepository,
    val unitID: Int
) : ViewModel() {
    private val words = repository.getWordsInUnit(unitID)
    private val trainer: LearnWordsTrainer = LearnWordsTrainer(words)

    private val _currentExercise = mutableStateOf(trainer.generateNextExercise())
    var currentExercise by _currentExercise
    private var elementStates = mutableStateListOf<ElementState>()
    private val _bottomPanelState = mutableStateOf(false)
    var bottomPanelState by _bottomPanelState

    fun resetAllStates() {
        elementStates.clear()
        trainer.getStatesNumber()?.let {
            repeat(it) {
                elementStates.add(ElementState.Neutral)
            }
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
        val correct = trainer.checkAnswer(answer)
        if (correct) {
            when (val ex = currentExercise) {
                is Exercise.MatchPairsQuestion -> { ex.options.forEach { trainer.setLearned(it) } }
                is Exercise.IsCorrectTranslationQuestion -> { trainer.setLearned(ex.correctWord) }
                is Exercise.RightOptionQuestion -> { trainer.setLearned(ex.correctAnswer) }
            }
        }
        return correct
    }

    fun isDone(): Boolean {
        return trainer.currentExercise.done()
    }

    sealed class ElementState {
        data object Neutral : ElementState()
        data object Correct : ElementState()
        data object Wrong : ElementState()
    }
}