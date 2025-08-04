package vbshkn.android.jetmemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import vbshkn.android.jetmemo.ui.Router

@Composable
fun UnitScreen(
    id: Int,
    controller: NavController
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Text(
                text = "The unit ID is $id"
            )
            Spacer(Modifier.size(10.dp))
            Button(
                onClick = {
                    controller.navigate(Router.HomeRoute) {
                    popUpTo<Router.HomeRoute>()
                }}
            ) {
                Text("Back")
            }
        }
    }
}