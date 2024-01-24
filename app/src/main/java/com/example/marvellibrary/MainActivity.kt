package com.example.marvellibrary

import CharactersBottomNav
import CollectionScreen
import LibraryScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marvellibrary.ui.theme.MarvelLibraryTheme
import com.example.marvellibrary.viewmodel.MarvelApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    data object Library : Destination("library")
    data object Collection : Destination("collection")
    data object CharacterDetail : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MarvelApiViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharactersScaffold(navController = navController)
                }
            }
        }
    }

    @Composable
    fun CharactersScaffold(navController: NavHostController) {
        val scaffoldState = rememberScaffoldState();
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = { CharactersBottomNav(navController = navController) }
        ) { paddingValues ->
            PaddingValues(horizontal = paddingValues.calculateBottomPadding())
            NavHost(
                navController = navController,
                startDestination = Destination.Library.route
            ) {
                composable(Destination.Library.route) {
                    LibraryScreen(
                        navController = navController,
                        viewModel = viewModel,
                        paddingValues = paddingValues
                    )
                }
                composable(Destination.Collection.route) {
                    CollectionScreen()
                }
                composable(Destination.CharacterDetail.route) { navBackStackEntry ->

                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MarvelLibraryTheme {

    }
}