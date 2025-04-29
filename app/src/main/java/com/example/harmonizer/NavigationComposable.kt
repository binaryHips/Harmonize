package com.example.harmonizer

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(name:String = "unnamed page") {
    CenterAlignedTopAppBar(
        title = {
            Text(name)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            //containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(onClick = {
                // navigation here
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopBar(name:String = "unnamed page", page: @Composable () -> (Unit)){

    Scaffold(
        modifier = Modifier.fillMaxSize().paint(
            painterResource(R.drawable.background),
            contentScale = ContentScale.FillHeight
        ),
        containerColor = Color.Transparent,
        topBar = { TopBarContent(name) }
    ) { paddingvalues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingvalues.calculateTopPadding()),
            color = Color.Transparent
        ) {
            page()
        }


    }

}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar {  Text("Hi i'm the page content")}
}