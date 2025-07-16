package vbshkn.android.jetmemo.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import vbshkn.android.jetmemo.ui.theme.JetMemoTheme

class LearnWordsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetMemoTheme {

            }
        }
    }
}