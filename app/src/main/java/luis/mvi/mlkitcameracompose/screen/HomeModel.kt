package luis.mvi.mlkitcameracompose.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import luis.mvi.mlkitcameracompose.navigation.AppNavigator
import luis.mvi.mlkitcameracompose.navigation.MyScreen
import javax.inject.Inject

@HiltViewModel
class HomeModel @Inject constructor(
    private val direction: HomeDirection
):ViewModel() {
    fun navigate(screen: MyScreen){
        viewModelScope.launch {
            direction.navigateScreen(screen)
        }
    }
}
interface HomeDirection{
    suspend fun navigateScreen(screen: MyScreen)
}
class HomeDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
): HomeDirection {
    override suspend fun navigateScreen(screen: MyScreen) {
        navigator.navigateTo(screen)
    }
}