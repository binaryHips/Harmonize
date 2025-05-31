package com.example.harmonizer

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.content.ContentValues
import android.provider.MediaStore
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.harmonizer.ui.theme.HarmonizerTheme


/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarmonizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Gallery(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 1)
            }
        }

        super.onCreate(savedInstanceState)
        setContent {
            //PhotoGalleryScreen()
            AppNavigator()
            //LoginPage()

        }
    }
}


@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: GalleryViewModel = viewModel(
        factory = GalleryViewModelFactory(context.applicationContext as Application)
    )

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            viewModel.refresh()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content: NavHost fills screen
        NavHost(
            navController,
            startDestination = "gallery",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("gallery") {
                PhotoGalleryScreen(navController, viewModel)
            }
            composable(
                "detail/{photoId}",
                arguments = listOf(navArgument("photoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val photoId = backStackEntry.arguments?.getInt("photoId") ?: return@composable
                val photo = viewModel.photos.collectAsState().value.find { it.id == photoId }
                if (photo != null) {
                    PhotoDetailScreen(photo, navController, viewModel)
                }
            }
        }

        // Floating "Take Photo" button at the bottom center
        Button(
            onClick = {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera")
                }
                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                )
                photoUri = uri
                if (uri != null) {
                    takePictureLauncher.launch(uri)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Take Photo")
        }
    }
}







