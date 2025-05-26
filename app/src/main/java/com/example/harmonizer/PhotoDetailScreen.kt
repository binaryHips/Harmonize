package com.example.harmonizer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(photo: PhotoItem, navController: NavController, viewModel: GalleryViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Photo Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.deletePhotoById(photo.id)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Photo")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(photo.title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text(photo.date, style = MaterialTheme.typography.labelMedium, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(photo.url),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            )

            Button(
                onClick = { /* Handle re-harmonize */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Re-harmonize", fontSize = 18.sp)
            }

            Button(
                onClick = {
                    viewModel.deletePhotoById(photo.id)
                    navController.popBackStack()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Delete", fontSize = 18.sp)
            }


        }

    }
}
