package luis.mvi.mlkitcameracompose

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import luis.mvi.mlkitcameracompose.ui.theme.MLKitCameraComposeTheme
import luis.mvi.mlkitcameracompose.navigation.NavigationHandler
import luis.mvi.mlkitcameracompose.screen.HomeScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationHandler: NavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        setContent {
            MLKitCameraComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(screen = HomeScreen()){ navigator ->
                        LaunchedEffect(navigator){
                            navigationHandler.navigatorBuffer
                                .onEach { it.invoke(navigator) }
                                .launchIn(lifecycleScope)
                        }
                        CurrentScreen()
                    }
                }
            }
        }
    }

    private fun checkPermission() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.areAllPermissionsGranted()) {
                            // Ruxsat berildi
                            // Notificationlarni chiqaring
                        }
                        if (it.isAnyPermissionPermanentlyDenied) {
                            // Foydalanuvchi ruxsat berishni rad qildi
                            // Xodimiz qo'shimcha ma'lumot yoki qo'llanma ko'rsating
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener { error ->
                // Xatolik sodir bo'ldi
            }
            .onSameThread()
            .check()
    }
}
