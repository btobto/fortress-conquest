package com.example.fortressconquest.ui.components

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.fortressconquest.BuildConfig
import com.example.fortressconquest.R
import java.io.File
import android.Manifest

@Composable
fun TakeImageButton(
    onImageTaken: (Uri) -> Unit,
    onCameraPermissionDenied: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uri = getImageUri(context)

    var imageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            imageUri = if (it) uri else Uri.EMPTY
            onImageTaken(imageUri)
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                cameraLauncher.launch(uri)
            } else {
                onCameraPermissionDenied()
            }
        }
    )

    FilledTonalButtonWithIcon(
        onClick = {
            val permissionGranted =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionGranted == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        icon = Icons.Outlined.Image,
        labelId = R.string.button_take_picture,
        modifier = modifier
    )
}

private fun getImageUri(context: Context): Uri {
    val directory = File(context.cacheDir, "images").also { it.mkdirs() }
    val file = File.createTempFile(
        "taken_image_",
        ".jpg",
        directory
    )

    return FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    )
}