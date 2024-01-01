package com.chetan.ff

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FFApp : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}