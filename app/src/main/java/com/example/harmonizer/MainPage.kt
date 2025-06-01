package com.example.harmonizer

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.harmonizer.ui.theme.HarmonizerTheme
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext


@Composable
fun MainPage(modifier: Modifier = Modifier) {
    val navController = (LocalActivity.current as MainActivity).navController
    val context = LocalContext.current

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Optional: show a Toast or navigate to gallery
            Toast.makeText(context, "Photo saved!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate(Screen.Harmonize()) },
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                ) {
                    Text("New Harmonization", fontSize = 18.sp)
                }

                Button(
                    onClick = {
                        navController.navigate(Screen.PhotoGallery)
                    },
                    modifier = Modifier.padding(bottom = 86.dp)
                ) {
                    Text("Harmonizer Gallery", fontSize = 18.sp)
                }

                Button(
                    onClick = { /* Handle account creation */ },
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text("Account Settings", fontSize = 18.sp)
                }
            }
        }

        // Take Photo Button at Bottom Center
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
                if (uri != null) takePictureLauncher.launch(uri)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Take Photo")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    HarmonizerTheme {
        MainPage()
    }
}
