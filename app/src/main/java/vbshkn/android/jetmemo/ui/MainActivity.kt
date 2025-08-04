package vbshkn.android.jetmemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import vbshkn.android.jetmemo.model.MainActivityViewModel
import vbshkn.android.jetmemo.model.MainActivityViewModelFactory
import vbshkn.android.jetmemo.ui.screen.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as App
        val repository = app.unitRepository
        val viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(repository),
        ) [MainActivityViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            HomeScreen(viewModel)
        }
    }
}