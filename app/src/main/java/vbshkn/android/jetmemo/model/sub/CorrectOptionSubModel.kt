package vbshkn.android.jetmemo.model.sub

import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearningScreenModel
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.WrongRed
import kotlin.random.Random

class CorrectOptionSubModel(private val baseModel: LearningScreenModel): LearningScreenSubModel {
    private val coff = Random.nextInt(0,100)
    private val ex = baseModel.currentExercise.value as Exercise.CorrectOptionExercise
    val qWord = if(coff < 50) ex.correctAnswer.original else ex.correctAnswer.translation
    val opWords = ex.options.map { if(coff < 50) it.translation else it.original }
    val elementStates get() = baseModel.elementStates

    fun onClicked(index: Int) {
        val answer = Answer.WordPair(qWord, opWords[index])
        val result = baseModel.checkAnswer(answer)
        val state = if(result) ElementStateDefaults.Correct else ElementStateDefaults.Wrong
        lockInterface()
        baseModel.updateStateAt(index, state.color, state.clickable)
        baseModel.showBottomBar(result)
    }

    private fun lockInterface() {
        for (i in 0..<baseModel.elementStates.value.size) {
            val state = ElementStateDefaults.Locked
            baseModel.updateStateAt(i, state.color, state.clickable)
        }
    }

    override fun resetAllStates() {
        baseModel.resetAllStates(ElementStateDefaults.Neutral)
    }

    class ElementStateDefaults {
        companion object {
            val Neutral = LearningScreenModel.ElementState(OptionTextGrey, true)
            val Locked = LearningScreenModel.ElementState(OptionTextGrey, false)
            val Correct = LearningScreenModel.ElementState(CorrectGreen, false)
            val Wrong = LearningScreenModel.ElementState(WrongRed, false)
        }
    }
}