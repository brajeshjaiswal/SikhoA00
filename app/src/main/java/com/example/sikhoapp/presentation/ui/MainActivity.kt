package com.example.sikhoapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sikhoapp.model.AnimeListResponse
import com.example.sikhoapp.presentation.ui.theme.SikhoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SikhoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier) {
    val mainActivityViewModel: MainActivityViewModel = hiltViewModel()
    val navController: NavHostController = rememberNavController()
    NavHost(modifier = modifier, navController = navController, startDestination = "show_anime") {
        composable("show_anime") {
            ShowAnime(
                navController = navController,
                loader = mainActivityViewModel.loader.value,
                list = mainActivityViewModel.animeListResponse.value?.data
            )
        }

        composable(
            route = "show_anime_detail/{animeId}",
            arguments = listOf(navArgument("animeId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("animeId")
            ShowAnimeDetail(id, mainActivityViewModel) {
                mainActivityViewModel.loader.value = false
                mainActivityViewModel.animeDetailResponse.value = null
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun ShowAnime(
    navController: NavHostController,
    loader: Boolean,
    list: List<AnimeListResponse.AnimeData>?
) {
    ShowLoader(loader = loader)
    Surface {
        LazyColumn {
            items(list.orEmpty()) { item ->
                ShowAnimeList(item) {
                    navController.navigate("show_anime_detail/${item.mal_id}")
                }
            }
        }
    }

}

@Composable
fun ShowLoader(loader: Boolean) {
    if (loader) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SikhoAppTheme {
        // ShowAnimeList(n)
    }
}