package vbshkn.android.jetmemo.model

import androidx.collection.mutableFloatSetOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import vbshkn.android.jetmemo.R
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
    private val _correctMessagePanelState = mutableStateOf(CorrectMessagePanelState())

    var currentQuestion by _currentQuestion
    val buttonStates: List<ButtonState> = _buttonStates
    var correctMessagePanelState by _correctMessagePanelState

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
        setCorrectMessagePanelState(isCorrect)
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

    fun setCorrectMessagePanelState(state: Boolean){
        correctMessagePanelState.switchState(state)
    }

    fun setCorrectMessagePanelVisible(visibility: Boolean){
        correctMessagePanelState.updateVisibility(visibility)
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

class CorrectMessagePanelState(){
    val _messageResource = mutableIntStateOf(R.string.title_correct)
    val _pictureRecource = mutableIntStateOf(R.drawable.ic_correct)
    val _mainColor = mutableStateOf(CorrectGreen)
    val _visibility = mutableFloatStateOf(1f)

    var messageResource by _messageResource
    var pictureResource by _pictureRecource
    var mainColor by _mainColor
    var visibility by _visibility

    fun switchState(correct: Boolean){
        when(correct){
            true -> {
                messageResource = R.string.title_correct
                pictureResource = R.drawable.ic_correct
                mainColor = CorrectGreen
            }
            false -> {
                messageResource = R.string.title_wrong
                pictureResource = R.drawable.ic_wrong
                mainColor = WrongRed
            }
        }
    }

    fun updateVisibility(visible: Boolean){
        visibility = if(visible) 1f else 0f
    }
}