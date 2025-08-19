package vbshkn.android.jetmemo.model

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearnScreenModel.ElementState
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.WrongRed

class MatchPairsSubModel(private val baseModel: LearnScreenModel) {
    val ex = baseModel.currentExercise.value as Exercise.MatchPairsExercise
    val leftColumnWords = ex.options.shuffled().map { it.original }
    val rightColumnWords = ex.options.shuffled().map { it.translation }
    private val COLUMN_SIZE = leftColumnWords.size
    val leftColumnStates = baseModel.elementStates
        .map { it.take(COLUMN_SIZE) }
        .stateIn(
            scope = baseModel.viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    val rightColumnStates = baseModel.elementStates
        .map { it.drop(COLUMN_SIZE)}
        .stateIn(
            scope = baseModel.viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    init {
        for (i in 0..<COLUMN_SIZE){
            updateLeftColumnState(i, ElementStateDefaults.Default)
            updateRightColumnState(i, ElementStateDefaults.Default)
        }
    }

    fun updateLeftColumnState(index: Int, state: ElementState) {
        if (index in 0..<COLUMN_SIZE) {
            baseModel.updateStateAt(index, state.color, state.clickable)
        }
    }

    fun updateRightColumnState(index: Int, state: ElementState) {
        if (index in 0..<COLUMN_SIZE) {
            val actualIndex = index + COLUMN_SIZE
            baseModel.updateStateAt(actualIndex, state.color, state.clickable)
        }
    }

    class ElementStateDefaults{
        companion object{
            val Default = ElementState(Color.Black, true)
            val Locked = ElementState(OptionTextGrey, false)
            val Correct = ElementState(CorrectGreen, false)
            val Wrong = ElementState(WrongRed, false)
        }
    }
}