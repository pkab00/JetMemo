package vbshkn.android.jetmemo.model.sub

import androidx.compose.ui.graphics.Color
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearnScreenModel

class ApproveTranslationSubModel(private val baseModel: LearnScreenModel) : LearnScreenSubModel {

    fun onClicked(isCorrectTranslation: Boolean) {
        val answer = Answer.YesNo(isCorrectTranslation)
        val result = baseModel.checkAnswer(answer)
        baseModel.showBottomBar(result)
        lockInterface()
    }

    override fun resetAllStates() {
        baseModel.resetAllStates(ElementStateDefaults.On)
    }

    fun isClickable(index: Int): Boolean {
        if (index in 0..<baseModel.elementStates.value.size) {
            return baseModel.stateAt(index)?.clickable ?: false
        }
        return false
    }

    private fun lockInterface() {
        for (i in 0..<baseModel.elementStates.value.size) {
            val state = ElementStateDefaults.Off
            baseModel.updateStateAt(i, state.color, state.clickable)
        }
    }

    class ElementStateDefaults {
        companion object {
            val On = LearnScreenModel.ElementState(Color.Unspecified, true)
            val Off = LearnScreenModel.ElementState(Color.Unspecified, false)
        }
    }
}