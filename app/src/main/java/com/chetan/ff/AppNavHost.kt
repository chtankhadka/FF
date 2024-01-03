package com.chetan.ff

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chetan.ff.presentation.comment.CommentScreen
import com.chetan.ff.presentation.comment.CommentViewModel
import com.chetan.ff.presentation.dashboard.DashboardScreen
import com.chetan.ff.presentation.dashboard.DashboardViewModel
import com.chetan.ff.presentation.google_sign_in.GoogleAuthUiClient
import com.chetan.ff.presentation.google_sign_in.SignInScreen
import com.chetan.ff.presentation.google_sign_in.SignInViewModel
import com.chetan.ff.presentation.musicplayer.MusicPlayerScreen
import com.chetan.ff.utils.CleanNavigate.cleanNavigate
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    onBack: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
){
   NavHost(
       navController = navController,
       startDestination = Destination.Screen.SignInDestination.route,
   ){


       composable(Destination.Screen.SignInDestination.route) {
           val viewModel = hiltViewModel<SignInViewModel>()
           val state by viewModel.state.collectAsStateWithLifecycle()

           LaunchedEffect(key1 = Unit, block = {
               if (googleAuthUiClient.getSignedInUser() != null) {
                   println(googleAuthUiClient.getSignedInUser()!!.userEmail)
                   if (googleAuthUiClient.getSignedInUser()!!.userEmail == "momobarnextin@gmail.com") {
                       navController.cleanNavigate(Destination.Screen.DashboardDestination.route)
                   } else {
                       navController.cleanNavigate(Destination.Screen.DashboardDestination.route)
                   }
               }
           })
           val launcher =
               rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                   onResult = { result ->
                       if (result.resultCode == ComponentActivity.RESULT_OK) {
                           lifecycleScope.launch {
                               val signInResult = googleAuthUiClient.signInWithIntent(
                                   intent = result.data ?: return@launch
                               )
                               viewModel.onSignInResult(signInResult)
                           }
                       }

                   })

           LaunchedEffect(key1 = state.isSignInSuccessful, block = {
               if (state.isSignInSuccessful) {
                   viewModel.resetState()
                   if (googleAuthUiClient.getSignedInUser()!!.userEmail == "momobarnextin@gmail.com") {
                       navController.cleanNavigate(Destination.Screen.DashboardDestination.route)
                   } else {
                       navController.cleanNavigate(Destination.Screen.DashboardDestination.route)
                   }

               }
           })
           SignInScreen(state = state) {
               lifecycleScope.launch {
                   val signInIntentSender = googleAuthUiClient.signIn()
                   launcher.launch(
                       IntentSenderRequest.Builder(
                           signInIntentSender ?: return@launch
                       ).build()
                   )
               }
           }
       }
       composable(Destination.Screen.DashboardDestination.route){
           val viewModel = hiltViewModel<DashboardViewModel>()
           val state by viewModel.state.collectAsStateWithLifecycle()
           DashboardScreen(
               nav = navController,
               onBack = onBack,
               state = state,
               onEvent = viewModel.onEvent

           )
       }
       composable(Destination.Screen.CommentDestination.route){
           val img_id = it.arguments?.getString("img_id")!!
           val viewModel = hiltViewModel<CommentViewModel>()
           val state by viewModel.state.collectAsStateWithLifecycle()
           CommentScreen(
               nav = navController,
               state = state,
               onEvent = viewModel.onEvent,
               id = img_id.split("@")

           )
       }

       composable(Destination.Screen.MusicPlayerDestination.route){
           MusicPlayerScreen(
               nav = navController
           )
       }
   }
}