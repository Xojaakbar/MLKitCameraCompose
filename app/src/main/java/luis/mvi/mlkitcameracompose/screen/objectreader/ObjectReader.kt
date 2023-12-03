package luis.mvi.mlkitcameracompose.screen.objectreader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.mlkit.vision.objects.DetectedObject
import luis.mvi.mlkitcameracompose.components.CameraView
import luis.mvi.mlkitcameracompose.components.DrawDetectedObjects
import luis.mvi.mlkitcameracompose.components.analizer.ObjectDetectionAnalyzer
import luis.mvi.mlkitcameracompose.navigation.MyScreen

class ObjectReader(): MyScreen() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val detectedObjects = remember { mutableStateListOf<DetectedObject>() }

        val screenWidth = remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
        val screenHeight = remember { mutableStateOf(context.resources.displayMetrics.heightPixels) }

        val imageWidth = remember { mutableStateOf(480) }
        val imageHeight = remember { mutableStateOf(640) }

        Box(modifier = Modifier.fillMaxSize()) {
            CameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                analyzer = ObjectDetectionAnalyzer { objects, width, height ->
                    detectedObjects.clear()
                    detectedObjects.addAll(objects)
                    imageWidth.value = width
                    imageHeight.value = height
                }
            )

            DrawDetectedObjects(
                detectedObjects,
                imageHeight.value,
                imageWidth.value,
                screenWidth.value,
                screenHeight.value
            )
        }
    }
}
