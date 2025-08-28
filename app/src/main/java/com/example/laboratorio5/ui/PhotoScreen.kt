package com.example.laboratorio5.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laboratorio5.R
import com.example.laboratorio5.util.UriResolver
import com.example.laboratorio5.viewmodel.PhotosViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(
    viewModel: PhotosViewModel = viewModel()
) {
    val context = LocalContext.current
    val photos by viewModel.photos.collectAsStateWithLifecycle()

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val name = UriResolver.getDisplayName(context, it)
            viewModel.addFromUri(it, name)
        }
    }

    val capturedUri = remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedUri.value?.let {
                val name = UriResolver.getDisplayName(context, it)
                viewModel.addFromUri(it, name)
            }
        }
    }

    fun launchCamera() {
        val file = createImageFile(context)
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)
        capturedUri.value = uri
        takePictureLauncher.launch(uri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_photos)) },
                actions = {
                    // Acción simple en AppBar para la cámara
                    TextButton(onClick = { launchCamera() }) {
                        Text(text = stringResource(id = R.string.camera))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Text(text = "+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { innerPadding: PaddingValues ->
        PhotoGrid(
            items = photos,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", imagesDir)
}