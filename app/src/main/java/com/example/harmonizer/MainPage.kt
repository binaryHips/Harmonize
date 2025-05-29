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


@Composable
fun MainPage(modifier: Modifier = Modifier) {
    val navController = (LocalActivity.current as MainActivity).navController
    Box(
        modifier = Modifier
        .fillMaxSize()
    ) {

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
                    onClick = {  },
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
                    Text(" Harmonizer Gallery  ", fontSize = 18.sp)
                }

                Button(
                    onClick = { /* Handle account creation */ },
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text("Account Settings", fontSize = 18.sp)
                }
            }
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
