package vbshkn.android.jetmemo.model

import androidx.compose.runtime.getValue
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
    private val _borderColor = mutableStateOf(BorderGrey)
    private val _smallButtonColor = mutableStateOf(Grey10)
    private val _textColor = mutableStateOf(OptionTextGrey)
    private val _numberTextColor = mutableStateOf(OptionTextGrey)

    var currentQuestion by _currentQuestion
    var borderColor by _borderColor
    var smallButtonColor by _smallButtonColor
    var textColor by _textColor
    var numberTextColor by _numberTextColor

    fun setNeutralState(){
        _borderColor.value = BorderGrey
        _smallButtonColor.value = Grey10
        _textColor.value = OptionTextGrey
        _numberTextColor.value = OptionTextGrey
    }

    fun setCorrectState(){
        _borderColor.value = CorrectGreen
        _smallButtonColor.value = CorrectGreen
        _textColor.value = CorrectGreen
        _numberTextColor.value = Color.White
    }

    fun setWrongState(){
        _borderColor.value = WrongRed
        _smallButtonColor.value = WrongRed
        _textColor.value = WrongRed
        _numberTextColor.value = Color.White
    }
}