package com.chetan.ff

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.chetan.ff.presentation.google_sign_in.GoogleAuthUiClient
import com.chetan.ff.service.AudioService
import com.chetan.ff.ui.theme.FFTheme
import com.chetan.orderdelivery.data.local.Preference
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preference: Preference
    var isServiceRunning = false
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private fun startService(){
        if (!isServiceRunning){
            val intent = Intent(this,AudioService::class.java)
            startForegroundService(intent)
        }else{
            startService(intent)
        }
        isServiceRunning = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FFTheme(darkTheme = false) {
                val linear = Brush.linearGradient(listOf(Color.Red, Color.Blue))

                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    Color.White
                                ),

                                )
                        )

                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController,
                        onBack = {
                            finish()
                        },
                        audioService = {
                            startService()
                        },
                        googleAuthUiClient,
                        lifecycleScope,
                        applicationContext
                        )
                }
            }
        }
    }
}

