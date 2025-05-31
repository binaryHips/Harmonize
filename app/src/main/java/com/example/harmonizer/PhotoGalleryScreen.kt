package com.example.harmonizer

import android.media.Image
import androidx.activity.compose.LocalActivity
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.harmonizer.ui.theme.HarmonizerTheme


@Composable
fun PhotoGalleryScreen() {
    val navController = (LocalActivity.current as MainActivity).navController
    val viewModel = (LocalActivity.current as MainActivity).gallery
    val photos by viewModel.photos.collectAsState()
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 60.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos) { photo ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 3.dp, vertical = 40.dp)
                    .clickable {
                        val encodedUrl = Uri.encode(photo.url)
                        val encodedTitle = Uri.encode(photo.title)
                        val encodedDate = Uri.encode(photo.date)
                        //navController.navigate("detail/$encodedUrl/$encodedTitle/$encodedDate")
                        navController.navigate(Screen.PhotoVisualizer(photo.id))

                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(photo.url),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(photo.title, style = MaterialTheme.typography.bodyMedium)
                Text(photo.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GallPreview() {
    HarmonizerTheme {
        //PhotoGalleryScreen(navController)
    }
}
