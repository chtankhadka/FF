package com.chetan.ff

open class Destination(open val route: String) {
    object Screen{

        object DashboardDestination : Destination("dashboard-screen")


        //music player
        object MusicPlayerDestination : Destination("music-player-screen")


    }
}