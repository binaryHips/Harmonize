package com.example.harmonizer

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    val viewModel: GalleryViewModel = viewModel() // shared instance

    NavHost(navController, startDestination = "gallery") {
        composable("gallery") {
            //PhotoGalleryScreen(navController, viewModel)
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
}







