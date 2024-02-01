package com.example.marvellibrary

import CharacterDetailsScreen
import CharactersBottomNav
import CollectionScreen
import LibraryScreen
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marvellibrary.ui.theme.MarvelLibraryTheme
import com.example.marvellibrary.viewmodel.CollectionDbViewModel
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

    private val marvelApiViewModel by viewModels<MarvelApiViewModel>()
    private val collectionDbViewModel by viewModels<CollectionDbViewModel>()

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
                    CharactersScaffold(
                        navController = navController,
                        marvelApiViewModel = marvelApiViewModel,
                        collectionDbViewModel = collectionDbViewModel
                    )
                }
            }
        }
    }

    @Composable
    fun CharactersScaffold(
        navController: NavHostController,
        marvelApiViewModel: MarvelApiViewModel,
        collectionDbViewModel: CollectionDbViewModel
    ) {
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
                        viewModel = marvelApiViewModel,
                        paddingValues = paddingValues
                    )
                }
                composable(Destination.Collection.route) {
                    CollectionScreen(collectionDbViewModel, navController)
                }
                composable(Destination.CharacterDetail.route) { navBackStackEntry ->
                    val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                    if (id == null) {
                        Toast.makeText(
                            LocalContext.current,
                            "Character id required",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        marvelApiViewModel.retrieveSingleCharacter(id)
                        CharacterDetailsScreen(
                            marvelApiViewModel = marvelApiViewModel,
                            collectionDbViewModel = collectionDbViewModel,
                            paddingValues = paddingValues,
                            navController = navController
                        )
                    }
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