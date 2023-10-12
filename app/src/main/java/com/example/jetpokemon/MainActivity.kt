package com.example.jetpokemon

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpokemon.ui.screens.PokemonDetailScreen
import com.example.jetpokemon.ui.screens.PokemonListScreen
import com.example.jetpokemon.ui.theme.JetpokemonTheme
import com.example.jetpokemon.ui.viewmodels.PokemonDetailViewModel
import com.example.jetpokemon.ui.viewmodels.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("RememberReturnType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpokemonTheme {
                val navController = rememberNavController()
                val viewModel: PokemonListViewModel by viewModels()
                val detailViewModel: PokemonDetailViewModel by viewModels()
                NavHost(navController = navController, startDestination = "pokemones") {
                    composable("pokemones") {
                        PokemonListScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }

                    composable(
                        "pokemon/{dominantColor}/{pokemonName}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("pokemonName") {
                                type = NavType.StringType
                            },
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(color) }
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                        PokemonDetailScreen(
                            pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                            dominantColor = dominantColor ?: Color.White,
                            navController = navController,
                            viewModel = detailViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpokemonTheme {
        Greeting("Android")
    }
}