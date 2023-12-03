package luis.mvi.mlkitcameracompose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.hilt.getViewModel
import luis.mvi.mlkitcameracompose.navigation.MyScreen
import luis.mvi.mlkitcameracompose.utils.Screens

class HomeScreen : MyScreen() {
    @Composable
    override fun Content() {
        val viewmodel = getViewModel<HomeModel>()
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Screens.values().onEach {
                TextButton(onClick = {
                    viewmodel.navigate(it.screen)
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = it.value)
                }
            }
        }
    }
}