package com.chetan.ff.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chetan.ff.R
import com.chetan.ff.presentation.dashboard.home.HomeScreen
import com.chetan.ff.presentation.dashboard.home.HomeViewModel
import com.chetan.ff.presentation.dashboard.library.LibraryScreen
import com.chetan.ff.utils.BottomNavigate.bottomNavigate

data class InnerPage(
    val route: String,
    val label: Int,
    val icon: ImageVector,
    val count: String = "",
    val isBadge: Boolean = false
)

@Composable
fun DashboardScreen(nav: NavHostController) {
    val bottomNavController = rememberNavController()
    val scope = rememberCoroutineScope()
    val items: List<InnerPage> = remember {
        listOf(
            InnerPage("home", R.string.home, Icons.Default.Home),
            InnerPage("library", R.string.library, Icons.Default.LibraryMusic),
            InnerPage(
                "musicplayer",
                R.string.musicplayer,
                Icons.Default.PlayCircleOutline,
                isBadge = true
            ),
            InnerPage("history", R.string.history, Icons.Default.Notifications)
        )
    }
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                items.forEach { screen ->
                    val isSelected =
                        navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true
                    val color = if (isSelected) Color.White else MaterialTheme.colorScheme.outline

                    CompositionLocalProvider(LocalContentColor provides color) {
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    LocalAbsoluteTonalElevation.current
                                )
                            ),
                            icon = {
                                Card(
                                    modifier = Modifier.size(34.dp),
                                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                    elevation = CardDefaults.cardElevation(10.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .size(20.dp),
                                            imageVector = screen.icon,
                                            tint = color,
                                            contentDescription = ""
                                        )
                                        Text(
                                            text = if (screen.isBadge) "" else "",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(end = 2.dp),
                                            fontSize = 8.sp,
                                            textAlign = TextAlign.Right,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                }
                            },
                            selected = isSelected,
                            onClick = { bottomNavController.bottomNavigate(screen.route) },
                            label = {},
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        },
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = bottomNavController,
            startDestination = "home"
        ) {
            composable("home") {
                val viewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    navController = nav,
                    event = viewModel.onEvent,
                    state = viewModel.state.collectAsStateWithLifecycle().value
                )
            }
            composable("library") {
                LibraryScreen(
                    navController = nav
                )
            }
        }
    }


}