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
fun PhotoDetailScreen(photoUrl: String, title: String, date: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Photo Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 60.dp, vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text(date, style = MaterialTheme.typography.labelMedium, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(photoUrl),
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
                onClick = { /* Handle delete */ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Delete", fontSize = 18.sp)
            }

        }

    }
}
