package vbshkn.android.jetmemo.model.sub

import androidx.compose.ui.graphics.Color
import vbshkn.android.jetmemo.logic.Answer
import vbshkn.android.jetmemo.logic.Exercise
import vbshkn.android.jetmemo.model.LearningScreenModel

/**
 * Саб-модель для управления ApproveTranslationView.
 * @param baseModel LearningScreenModel основного экрана
 * @see Exercise.ApproveTranslationExercise
 */
class ApproveTranslationSubModel(private val baseModel: LearningScreenModel) : LearningScreenSubModel {
    val exercise get() = baseModel.currentExercise

    /**
     * Коллбэк при нажатии на одну из двух кнопок (да/нет).
     * @param isCorrectTranslation соответствующий кнопке ответ пользователя
     */
    fun onClicked(isCorrectTranslation: Boolean) {
        val answer = Answer.YesNo(isCorrectTranslation)
        val result = baseModel.checkAnswer(answer)
        baseModel.showBottomBar(result)
        lockInterface()
    }

    /**
     * Переопределяемый метод сброса состояний элемента.
     * Устанавливает состояние ElementStateDefaults.On.
     */
    override fun resetAllStates() {
        baseModel.resetAllStates(ElementStateDefaults.On)
    }

    /**
     * Проверяет, является ли элемент кликабельным.
     * @param index индекс элемента
     * @return true если элемент кликабелен, иначе false
     */
    fun isClickable(index: Int): Boolean {
        if (index in 0..<baseModel.elementStates.value.size) {
            return baseModel.stateAt(index)?.clickable ?: false
        }
        return false
    }

    /**
     * Блокировка интерфейса по завершении упражнения.
     */
    private fun lockInterface() {
        for (i in 0..<baseModel.elementStates.value.size) {
            val state = ElementStateDefaults.Off
            baseModel.updateStateAt(i, state.color, state.clickable)
        }
    }

    /**
     * Набор возможных состояний элементов.
     */
    class ElementStateDefaults {
        companion object {
            val On = LearningScreenModel.ElementState(Color.Unspecified, true) // кликабельный
            val Off = LearningScreenModel.ElementState(Color.Unspecified, false) // не кликабельный
        }
    }
}