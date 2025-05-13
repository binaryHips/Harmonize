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
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "gallery") {
        composable("gallery") {
            PhotoGalleryScreen(navController)
        }
        composable(
            "detail/{photoUrl}/{title}/{date}",
            arguments = listOf(
                navArgument("photoUrl") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("photoUrl") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            PhotoDetailScreen(url, title, date, navController)
        }
    }
}






