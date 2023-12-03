package luis.mvi.mlkitcameracompose.utils

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.lifecycle.ProcessCameraProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun showLog(str:String){
    Log.d("ABCD", str)
}
//@RequiresApi(Build.VERSION_CODES.P)
//suspend fun Context.createVideoCaptureUseCase(
//    lifecycleOwner: LifecycleOwner,
//    cameraSelector: CameraSelector,
//    previewView: PreviewView
//): VideoCapture<Recorder> {
//    val preview = Preview.Builder()
//        .build()
//        .apply { setSurfaceProvider(previewView.surfaceProvider) }
//
//    val qualitySelector = QualitySelector.from(
//        Quality.FHD,
//        FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
//    )
//    val recorder = Recorder.Builder()
//        .setExecutor(mainExecutor)
//        .setQualitySelector(qualitySelector)
//        .build()
//    val videoCapture = VideoCapture.withOutput(recorder)
//
//    val cameraProvider = getCameraProvider()
//    cameraProvider.unbindAll()
//    cameraProvider.bindToLifecycle(
//        lifecycleOwner,
//        cameraSelector,
//        preview,
//        videoCapture
//    )
//
//    return videoCapture
//}
@RequiresApi(Build.VERSION_CODES.P)
suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            mainExecutor
        )
    }
}
