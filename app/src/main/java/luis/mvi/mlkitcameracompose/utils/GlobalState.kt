package luis.mvi.mlkitcameracompose.utils

import kotlinx.coroutines.flow.MutableStateFlow

object GlobalState {
    val TAKE_PIC = MutableStateFlow(false)
}