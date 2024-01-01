package com.chetan.ff

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chetan.ff.presentation.dashboard.DashboardScreen
import com.chetan.ff.presentation.musicplayer.MusicPlayerScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
){
   NavHost(
       navController = navController,
       startDestination = Destination.Screen.DashboardDestination.route,
   ){
       composable(Destination.Screen.DashboardDestination.route){
           DashboardScreen(
               nav = navController
           )
       }

       composable(Destination.Screen.MusicPlayerDestination.route){
           MusicPlayerScreen(
               nav = navController
           )
       }
   }
}