package com.chetan.ff.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object MyDate {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertStringToDate(timestamp: String): String {
        val instant = Instant.ofEpochMilli(timestamp.toLong())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    fun CurrentDateTimeSDF() : String{
        val millis = System.currentTimeMillis()
        val pattern = "yyyy-MM-dd hh:mm a"
        val date = Date(millis)
        val sdf = SimpleDateFormat(pattern,Locale.US)
        return sdf.format(date)
    }

    fun differenceOfDates(before: String, now: String): String {
        val duration = abs(1000 * before.toLong() - now.toLong())
        val days = TimeUnit.MILLISECONDS.toDays(duration)
        val hours = TimeUnit.MILLISECONDS.toHours(duration) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60

        return when {
            days != 0L -> if (days == 1L) "$days day ago" else "$days days ago"
            hours != 0L -> if (hours == 1L) "$hours hour ago" else "$hours hours ago"
            minutes != 0L -> if (minutes == 1L) "$minutes minute ago" else "$minutes minutes ago"
            else -> "just now"
        }
//
//        (days != 0L) {
//            if (days == 1L ){
//                "$days day ago"
//            }else{
//                "$days days ago"
//            }
//
//        } else if (hours != 0L) {
//            if (hours == 1L){
//                "$hours hour ago"
//            }else{
//                "$hours hours ago"
//            }
//
//        } else if (minutes != 0L) {
//            if (minutes == 1L){
//                "$minutes minute ago"
//            }else{
//                "$minutes minutes ago"
//            }
//
//        } else {
//            "just now"
//        }
    }
    fun differenceOfDatesNoMultiple(before: String, now: String): String {
        val duration = abs(before.toLong() - now.toLong())
        val days = TimeUnit.MILLISECONDS.toDays(duration)
        val hours = TimeUnit.MILLISECONDS.toHours(duration) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60

        return when {
            days != 0L -> if (days == 1L) "$days day ago" else "$days days ago"
            hours != 0L -> if (hours == 1L) "$hours hour ago" else "$hours hours ago"
            minutes != 0L -> if (minutes == 1L) "$minutes minute ago" else "$minutes minutes ago"
            else -> "just now"
        }
    }
}