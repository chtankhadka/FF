package com.chetan.ff.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.media.AudioManager
import android.os.BatteryManager
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.di.HiltEntryPoint
import com.chetan.orderdelivery.data.local.Preference
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.onesignal.OneSignal
import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationServiceExtension
    : INotificationServiceExtension {

    @Inject
    lateinit var preference: Preference

    @SuppressLint("MissingPermission")
    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        preference = Preference(event.context)
        if (event.notification.body != null) {
            try {
                val audioManager = event.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val batteryIntent: Intent? = event.context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

                val locationManager =
                    event.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                println(isGpsEnabled)
                val hiltEntryPoint =
                    EntryPointAccessors.fromApplication(event.context, HiltEntryPoint::class.java)
                val firestoreUseCases = hiltEntryPoint.firestoreUseCases()
                val weatherRepository = hiltEntryPoint.weatherRepository()
                CoroutineScope(SupervisorJob()).launch {
                    if (isGpsEnabled) {
                        val locationClient =
                            LocationServices.getFusedLocationProviderClient(event.context)

                        val priority = Priority.PRIORITY_HIGH_ACCURACY
                        val locResult =
                            locationClient.getCurrentLocation(
                                priority,
                                CancellationTokenSource().token
                            )
                                .await()
                        val level: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)?:-1
                        val scale: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

                        val batteryPercentage = (level / scale.toFloat() * 100).toInt().toString()
                        delay(1000)
                        println(locResult)
                        locResult?.let { currentLocation ->
                            val result = weatherRepository.getCurrentLocationKey(
                                currentLocation.latitude.toString(),
                                currentLocation.longitude.toString(),
                                "8c0b030035f977d2af594c1332309eb1"
                            )
                           firestoreUseCases.updateStatus(
                                data = UpdateStatusRequestResponse(
                                    id = preference.tableName ?: "common",
                                    temperature = ((result.main?.temp ?: 273.15) - 273.15).toInt()
                                        .toString(),
                                    address = if (result.name.length > 8) result.name.substring(
                                        0,
                                        7
                                    ) else result.name,
                                    country = result.sys.country,
                                    date = result.dt.toString(),
                                    weather = result.weather.first().main,
                                    group = preference.groupName ?: "test",
                                    userProfile = preference.gmailProfile ?: "",
                                    oneSignalId = OneSignal.User.pushSubscription.id,
                                    audioProfile = audioManager.ringerMode.toString(),
                                    batteryLife = "$batteryPercentage%"
                                )
                            )

                        }
                    }
                }
            } catch (e: Exception) {

            }
        }


    }
}