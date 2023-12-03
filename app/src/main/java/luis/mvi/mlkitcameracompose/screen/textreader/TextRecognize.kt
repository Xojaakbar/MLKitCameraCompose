package luis.mvi.mlkitcameracompose.screen.textreader

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import luis.mvi.mlkitcameracompose.components.CameraView
import luis.mvi.mlkitcameracompose.navigation.MyScreen
import luis.mvi.mlkitcameracompose.components.analizer.TextRecognitionAnalyzer

class TextRecognize : MyScreen() {
    @Composable
    override fun Content() {
        TextRecognizer()
    }

    @Composable
    fun TextRecognizer() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        var extractedText by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraView(context = context, analyzer = TextRecognitionAnalyzer {
                if (it != extractedText && it.isNotBlank()) {
                    extractedText = it
                    Log.d("TTT", extractedText)
                }
            }, lifecycleOwner = lifecycleOwner)
        }

    }
}