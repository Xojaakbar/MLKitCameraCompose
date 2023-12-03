package luis.mvi.mlkitcameracompose.navigation

import kotlinx.coroutines.flow.SharedFlow
import luis.mvi.mlkitcameracompose.navigation.NavigationArg

interface NavigationHandler {
    val navigatorBuffer:SharedFlow<NavigationArg>
}