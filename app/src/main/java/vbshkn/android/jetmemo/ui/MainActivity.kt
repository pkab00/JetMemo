package vbshkn.android.jetmemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vbshkn.android.jetmemo.model.HomeScreenModel
import vbshkn.android.jetmemo.model.HomeScreenModelFactory
import vbshkn.android.jetmemo.ui.screen.HomeScreen
import vbshkn.android.jetmemo.ui.screen.UnitScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val store = this.viewModelStore
        val app = application as App
        val repository = app.unitRepository

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var viewModel: ViewModel? = null

            NavHost(
                navController = navController,
                startDestination = Router.HomeRoute
            ) {
                composable<Router.HomeRoute> {
                    viewModel = ViewModelProvider(
                        store,
                        HomeScreenModelFactory(repository),
                    ) [HomeScreenModel::class.java]
                    HomeScreen(
                        viewModel = viewModel as HomeScreenModel,
                        controller = navController
                    )
                }
                composable<Router.UnitRoute>{
                    // данные из data class'а принимаем здесь
                    val data = it.toRoute<Router.UnitRoute>()
                    UnitScreen(
                        id = data.id,
                        controller = navController
                    )
                }
            }
        }
    }
}