package vbshkn.android.jetmemo.model.sub

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearningScreenModel
import vbshkn.android.jetmemo.model.LearningScreenModel.ElementState
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey

class MatchPairsSubModel(private val baseModel: LearningScreenModel)
    : LearningScreenSubModel {
    val ex = baseModel.currentExercise.value as Exercise.MatchPairsExercise
    val leftColumnWords = ex.options.shuffled().map { it.original }
    val rightColumnWords = ex.options.shuffled().map { it.translation }
    private val COLUMN_SIZE = leftColumnWords.size
    private var selectedWord: String? = null
    private var selectedIndex = -1
    val leftColumnStates = baseModel.elementStates
        .map { it.take(COLUMN_SIZE) }
        .stateIn(
            scope = baseModel.viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    val rightColumnStates = baseModel.elementStates
        .map { it.drop(COLUMN_SIZE) }
        .stateIn(
            scope = baseModel.viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private fun updateLeftColumnState(index: Int, state: ElementState) {
        if (index in 0..<COLUMN_SIZE) {
            baseModel.updateStateAt(index, state.color, state.clickable)
        }
    }

    private fun updateRightColumnState(index: Int, state: ElementState) {
        if (index in 0..<COLUMN_SIZE) {
            val actualIndex = index + COLUMN_SIZE
            baseModel.updateStateAt(actualIndex, state.color, state.clickable)
        }
    }

    private fun lockLeftColumn(lock: Boolean) {
        for (i in 0..<COLUMN_SIZE) {
            if (leftColumnStates.value[i] != ElementStateDefaults.Correct
                && leftColumnStates.value[i] != ElementStateDefaults.Selected
            ) {
                updateLeftColumnState(
                    i,
                    if (lock) ElementStateDefaults.Locked
                    else ElementStateDefaults.Default
                )
            }
        }
    }

    private fun lockRightColumn(lock: Boolean) {
        for (i in 0..<COLUMN_SIZE) {
            if (rightColumnStates.value[i] != ElementStateDefaults.Correct
                && rightColumnStates.value[i] != ElementStateDefaults.Selected
            ) {
                updateRightColumnState(
                    i,
                    if (lock) ElementStateDefaults.Locked
                    else ElementStateDefaults.Default
                )
            }
        }
    }

    fun onLeftClicked(index: Int) {
        val text = leftColumnWords[index]
        if (selectedWord == null) {
            updateLeftColumnState(index, ElementStateDefaults.Selected)
            lockLeftColumn(true)
            selectedWord = text
            selectedIndex = index
        } else {
            val correct = baseModel.checkAnswer(Answer.WordPair(selectedWord!!, text))
            if (correct) {
                updateLeftColumnState(index, ElementStateDefaults.Correct)
                updateRightColumnState(selectedIndex, ElementStateDefaults.Correct)
                if (baseModel.isDone()) {
                    baseModel.showBottomBar(true)
                }
            } else {
                baseModel.showBottomBar(false)
                updateRightColumnState(selectedIndex, ElementStateDefaults.Default)
            }
            selectedWord = null
            selectedIndex = -1
            lockRightColumn(false)
        }
    }

    fun onRightClicked(index: Int) {
        val text = rightColumnWords[index]
        if (selectedWord == null) {
            updateRightColumnState(index, ElementStateDefaults.Selected)
            lockRightColumn(true)
            selectedWord = text
            selectedIndex = index
        } else {
            val correct = baseModel.checkAnswer(Answer.WordPair(selectedWord!!, text))
            if (correct) {
                updateRightColumnState(index, ElementStateDefaults.Correct)
                updateLeftColumnState(selectedIndex, ElementStateDefaults.Correct)
                if (baseModel.isDone()) {
                    baseModel.showBottomBar(true)
                }
            } else {
                baseModel.showBottomBar(false)
                updateLeftColumnState(selectedIndex, ElementStateDefaults.Default)
            }
            selectedWord = null
            selectedIndex = -1
            lockLeftColumn(false)
        }
    }

    override fun resetAllStates() {
        baseModel.resetAllStates(ElementStateDefaults.Default)
    }

    class ElementStateDefaults {
        companion object {
            val Default = ElementState(Color.Black, true)
            val Locked = ElementState(OptionTextGrey, false)
            val Selected = ElementState(Color.Black, false)
            val Correct = ElementState(CorrectGreen, false)
        }
    }
}