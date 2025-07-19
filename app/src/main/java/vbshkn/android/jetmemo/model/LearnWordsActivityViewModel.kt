package vbshkn.android.jetmemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import vbshkn.android.jetmemo.logic.LearnWordsTrainer
import vbshkn.android.jetmemo.ui.theme.BorderGrey
import vbshkn.android.jetmemo.ui.theme.CorrectGreen
import vbshkn.android.jetmemo.ui.theme.Grey10
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.WrongRed

class LearnWordsActivityViewModel : ViewModel() {
    private val trainer: LearnWordsTrainer = LearnWordsTrainer()

    private val _currentQuestion = mutableStateOf(trainer.generateNextQuestion())
    private val _buttonStates = mutableStateListOf<ButtonState>().apply {
        repeat(4) {add(ButtonState())}
    }

    var currentQuestion by _currentQuestion
    val buttonStates: List<ButtonState> = _buttonStates

    fun setNewQuestion(){
        currentQuestion = trainer.generateNextQuestion()
    }

    fun changeButtonState(buttonIndex: Int){
        val isCorrect: Boolean = currentQuestion?.run {
            correctAnswer.original == answers[buttonIndex].original
        } ?: false

        when(isCorrect){
            true -> buttonStates[buttonIndex].setCorrectState()
            false -> buttonStates[buttonIndex].setWrongState()
        }
    }

    fun resetButtonStates(){
        buttonStates.forEach {state ->
            state.setNeutralState()
        }
    }

    fun setClickable(clickable: Boolean){
        buttonStates.forEach {state ->
            state.updateClickable(clickable)
        }
    }
}

class ButtonState(){
    var borderColor by mutableStateOf(BorderGrey)
    var smallButtonColor by mutableStateOf(Grey10)
    var textColor by mutableStateOf(OptionTextGrey)
    var numberColor by mutableStateOf(OptionTextGrey)
    var isClickable by mutableStateOf(true)
        private set

    fun setNeutralState(){
        borderColor = BorderGrey
        smallButtonColor = Grey10
        textColor = OptionTextGrey
        numberColor = OptionTextGrey
    }

    fun setCorrectState(){
        borderColor = CorrectGreen
        smallButtonColor = CorrectGreen
        textColor = CorrectGreen
        numberColor = Color.White
    }

    fun setWrongState(){
        borderColor = WrongRed
        smallButtonColor = WrongRed
        textColor = WrongRed
        numberColor = Color.White
    }

    fun updateClickable(clickable: Boolean){
        isClickable = clickable
    }
}