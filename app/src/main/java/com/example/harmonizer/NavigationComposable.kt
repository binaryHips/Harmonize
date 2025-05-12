package com.example.harmonizer

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(name:String = "unnamed page") {

    val navController = (LocalActivity.current as MainActivity).navController

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
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        })
}

@kotlinx.serialization.Serializable
sealed class Screen(@StringRes val title: Int, val showBackArrow: Boolean = true) {

    @kotlinx.serialization.Serializable
    data object Login : Screen(R.string.login_page_title)
    @kotlinx.serialization.Serializable
    data object Main : Screen(R.string.main_page_title, showBackArrow = false)
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopBar(navController:NavHostController){
    val backStackEntry by navController.currentBackStackEntryAsState()
    //val currentRoute by remember { derivedStateOf { currentBackStackEntry?.destination?.route ?: "Home" } }
    val currentScreen = remember {
        derivedStateOf {
            backStackEntry?.destination?.let {
                when (it.route) {
                    Screen.Login::class.qualifiedName -> Screen.Login


                    else -> Screen.Main

                }
            } ?: Screen.Main
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().paint(
            painterResource(R.drawable.background_image),
            contentScale = ContentScale.FillHeight
        ),
        containerColor = Color.Transparent,
        topBar = { TopBarContent( LocalActivity.current!!.getString(currentScreen.value.title)) }
    ) { paddingvalues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingvalues.calculateTopPadding()),
            color = Color.Transparent
        ) {
            NavHost(navController = navController, startDestination = Screen.Login) {
                composable<Screen.Login> { LoginPage( /* ... */ ) }
                composable<Screen.Main> { MainPage( /* ... */ ) }
                // Add more destinations similarly.
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar {  Text("Hi i'm the page content")}
}*/