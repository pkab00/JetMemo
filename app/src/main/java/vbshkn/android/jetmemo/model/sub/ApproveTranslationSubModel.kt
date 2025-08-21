package vbshkn.android.jetmemo.model.sub

import androidx.compose.ui.graphics.Color
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.model.LearningScreenModel

class ApproveTranslationSubModel(private val baseModel: LearningScreenModel) : LearningScreenSubModel {
    val exercise get() = baseModel.currentExercise

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
            val On = LearningScreenModel.ElementState(Color.Unspecified, true)
            val Off = LearningScreenModel.ElementState(Color.Unspecified, false)
        }
    }
}