package vbshkn.android.jetmemo.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.model.LearnWordsActivityViewModel
import vbshkn.android.jetmemo.ui.theme.BorderGrey
import vbshkn.android.jetmemo.ui.theme.Grey10
import vbshkn.android.jetmemo.ui.theme.OptionTextGrey
import vbshkn.android.jetmemo.ui.theme.VividBlue

class LearnWordsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                content = {innerPadding: PaddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(Color.White)
                    ) {
                        CloseButton()
                        QuestionWord("Galaxy")
                        OptionPanel()
                        SkipButton()
                    }
                }
            )
        }
    }
}

@Composable
fun CloseButton(){
    Image(
        painter = painterResource(R.drawable.ic_close),
        contentDescription = "Close button",
        modifier = Modifier
            .padding(start = 24.dp)
            .clickable(
                onClick = {}
            )
    )
}

@Composable
fun QuestionWord(
    text: String,
    viewModel: LearnWordsActivityViewModel = viewModel()
){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 96.dp)
            //.background(Color.Red)
    ) {
        viewModel.currentQuestion?.correctAnswer?.let {
            Text(
                text = it.original,
                fontSize = 50.sp,
                fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Option(
    number: Int,
    viewModel: LearnWordsActivityViewModel = viewModel()
){
    val index = number - 1
    Button(
        onClick = {
            if(viewModel.buttonStates[index].isClickable){
                viewModel.changeButtonState(index)
                viewModel.setClickable(false)
            }
        },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, viewModel.buttonStates[index].borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = 14.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if(viewModel.buttonStates[index].isClickable){
                        viewModel.changeButtonState(index)
                        viewModel.setClickable(false)
                    }
                },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(1.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = viewModel.buttonStates[index].smallButtonColor,
                    contentColor = viewModel.buttonStates[index].numberColor
                ),
                modifier = Modifier
                    .size(40.dp)
            ) {
                Text(
                    text = number.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_semibold)),
                )
            }
            Text(
                text = (viewModel.currentQuestion?.answers?.get(number-1)?.translation ?: ""),
                // TODO: ПОДУМАТЬ ЧТО ДЕЛАТЬ С ЭТИМИ УРОДСКИМИ NULL-SAFE ВЫЗОВАМИ
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.rubik_regular)),
                color = viewModel.buttonStates[index].textColor,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun OptionPanel(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Green)
            .padding(
                start = 32.dp,
                end = 32.dp,
                top = 60.dp
            )
    ){
        Option(1)
        Option(2)
        Option(3)
        Option(4)
    }
}

@Composable
fun SkipButton(
    viewModel: LearnWordsActivityViewModel = viewModel()
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Yellow)
    ){
        Button(
            onClick = {
                viewModel.resetButtonStates()
                viewModel.setClickable(true)
                viewModel.setNewQuestion()
            },
            colors = ButtonDefaults.buttonColors(VividBlue),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 60.dp,
                    bottom = 30.dp
                )
                .height(58.dp)
        ) {
            Text(
                text = stringResource(R.string.button_skip).uppercase(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//private fun CloseButtonPreview(){
//    CloseButton()
//}

//@Composable
//@Preview(showBackground = true)
//private fun QuestionWordPreview(){
//    QuestionWord("Galaxy")
//}

//@Composable
//@Preview(showBackground = true)
//private fun OptionPreview(){
//    Option(1, "Галактика")
//}

@Composable
@Preview(showBackground = true)
private fun OptionScreenPreview(){
    OptionPanel()
}

@Composable
@Preview(showBackground = true)
private fun SkipButtonPreview(){
    SkipButton()
}