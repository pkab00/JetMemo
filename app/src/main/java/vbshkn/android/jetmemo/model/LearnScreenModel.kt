package vbshkn.android.jetmemo.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.lifecycle.ViewModel
import vbshkn.android.jetmemo.data.LearnRepository
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.logic.LearnWordsTrainer

class LearnScreenModel(
    repository: LearnRepository,
    val unitID: Int
) : ViewModel() {
    private val words = repository.getWordsInUnit(unitID)
    private val trainer: LearnWordsTrainer = LearnWordsTrainer(words)
    val currentExercise get() = trainer.currentExercise

    private var elementStates = mutableStateListOf<ElementState>()
    private val _bottomBarState = mutableStateOf(false)
    var bottomBarState by _bottomBarState

    private val _showBottomBar =  mutableStateOf(false)
    var showBottomBar by _showBottomBar
    private val _showSkipButton = mutableStateOf(true)
    var showSkipButton by _showSkipButton

    init {
        nextExercise()
    }

    fun stateAt(index: Int): ElementState? {
        if (index >= 0 && index < elementStates.size){
            return elementStates[index]
        }
        return null
    }

    private fun resetAllStates() {
        elementStates.clear()
        trainer.getStatesNumber()?.let {
            repeat(it) {
                elementStates.add(ElementState())
            }
        }
    }

    fun showBottomBar(correct: Boolean) {
        bottomBarState = correct
        showBottomBar = true
        showSkipButton = false
    }

    fun hideBottomBar() {
        showBottomBar = false
        showSkipButton = true
    }


    fun nextExercise() {
        trainer.generateNextExercise()
        resetAllStates()
    }

    fun checkAnswer(answer: Answer): Boolean{
        val correct = trainer.checkAnswer(answer)
        if (correct) {
            when (val ex = currentExercise) {
                is Exercise.MatchPairsExercise -> { ex.options.forEach { trainer.setLearned(it) } }
                is Exercise.IsCorrectTranslationExercise -> { trainer.setLearned(ex.correctWord) }
                is Exercise.RightOptionExercise -> { trainer.setLearned(ex.correctAnswer) }
                else -> {}
            }
        }
        return correct
    }

    fun isDone(): Boolean {
        return trainer.currentExercise.done()
    }

    data class ElementState(
        var color: Color = Color.Unspecified,
        var clickable: Boolean = true
    )
}