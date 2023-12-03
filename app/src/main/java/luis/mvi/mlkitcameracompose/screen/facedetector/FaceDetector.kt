package luis.mvi.mlkitcameracompose.screen.facedetector


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.launch
import luis.mvi.mlkitcameracompose.components.CameraView
import luis.mvi.mlkitcameracompose.components.DrawFaces2
import luis.mvi.mlkitcameracompose.components.analizer.FaceDetectionAnalyzer
import luis.mvi.mlkitcameracompose.navigation.MyScreen
import luis.mvi.mlkitcameracompose.utils.GlobalState

class FaceDetector : MyScreen() {
    lateinit var imageCapture:ImageCapture
    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val faceSnapshotStateList = remember { mutableStateListOf<Face>() }

        val screenWidth = remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
        val screenHeight = remember { mutableStateOf(context.resources.displayMetrics.heightPixels) }

        val imageWidth = remember { mutableStateOf(0) }
        val imageHeight = remember { mutableStateOf(0) }

        imageCapture = ImageCapture.Builder().build()
        Box(modifier = Modifier.fillMaxSize()) {
            CameraView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                analyzer = FaceDetectionAnalyzer { face, width, height ->
                    faceSnapshotStateList.clear()
                    faceSnapshotStateList.addAll(face)
                    imageWidth.value = width
                    imageHeight.value = height
                }
            )
            DrawFaces2(faces = faceSnapshotStateList, imageHeight.value, imageWidth.value, screenWidth.value, screenHeight.value){
                lifecycleOwner.lifecycleScope.launch {
                    GlobalState.TAKE_PIC.emit(true)
                }
            }
        }
    }
}
