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
fun LoginPage(modifier: Modifier = Modifier) {

    val navController = (LocalActivity.current as MainActivity).navController
    val client = (LocalActivity.current as MainActivity).client

    if (client.authToken != "") {
        client.requestVerifyToken()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {


        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        Color(0xFFF0F0F0)  // light gray background
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        Color(0xFFF0F0F0)  // light gray background
                    )
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { onLoginButtonClicked(client, password, username) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Login", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onRegisterButtonClicked(client, password, username) },
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text("Create Account", fontSize = 18.sp)
                }
            }
        }
    }
}

fun onLoginButtonClicked(client: Client, password: String, username:String){
    client.requestToken(username, password)
}

fun onRegisterButtonClicked(client:Client, password: String, username: String){
    client.requestCreateAccount(username, password)
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    HarmonizerTheme {
        LoginPage()
    }
}
