package vbshkn.android.jetmemo.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vbshkn.android.jetmemo.R
import vbshkn.android.jetmemo.ui.theme.JetMemoTheme

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
                    ) {
                        CloseButton()
                        QuestionWord("Galaxy")
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
fun QuestionWord(text: String){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 96.dp)
    ) {
        Text(
            text = text,
            fontSize = 50.sp,
            fontFamily = FontFamily(Font(R.font.nunito_semibold)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CloseButtonPreview(){
    CloseButton()
}

@Composable
@Preview(showBackground = true)
private fun QuestionWordPreview(){
    QuestionWord("Galaxy")
}