package com.chetan.ff.presentation.dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.media.AudioManager
import android.os.BatteryManager
import android.provider.Settings
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.DoNotDisturbOnTotalSilence
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.ff.Destination
import com.chetan.ff.R
import com.chetan.ff.data.model.weather.UpdateStatusRequestResponse
import com.chetan.ff.utils.LoadLottieAnimation
import com.chetan.ff.utils.MyDate
import com.chetan.ff.utils.RequestPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.absoluteValue

@SuppressLint("MissingPermission")
@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    event: (event: HomeEvent) -> Unit,
    state: HomeState
) {
    //location
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val batteryIntent: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))


    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    if (isGpsEnabled)
    var canOrder by remember {
        mutableStateOf(false)
    }
    if (!isGpsEnabled) {
        val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val requestCode = 0 // You can choose a unique request code
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            locationSettingsIntent,
            PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE to comply with Android S+
        )
        pendingIntent.send()
    } else {
        RequestPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            permissionGranted = {
                canOrder = it
            })
    }
    LaunchedEffect(key1 = canOrder, block = {
        if (canOrder) {
            val level: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)?:-1
            val scale: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

            val batteryPercentage = (level / scale.toFloat() * 100).toInt().toString()
            event(HomeEvent.AudioProfile(audioManager.ringerMode.toString(),"$batteryPercentage%"))
            scope.launch(Dispatchers.IO) {
                val locationClient =
                    LocationServices.getFusedLocationProviderClient(context)

                val priority = Priority.PRIORITY_HIGH_ACCURACY
                val result =
                    locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                        .await()
                result?.let { currentLocation ->
                    event(
                        HomeEvent.GetLocationWeatherInfo(
                            "${currentLocation.latitude}",
                            "${currentLocation.longitude}"
                        )
                    )

                }
            }
        }
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .animateContentSize(),
    ) {
        val pagerState = rememberPagerState {
            state.ffLocations.size
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ff", fontSize = 50.sp, fontFamily = FontFamily.Default,
//            fontFamily = FontFamily(
//                Font(
//                    familyName = DeviceFontFamilyName("nepal"),
//                    variationSettings = FontVariation.Settings(
//                        FontVariation.weight(950),
//                        FontVariation.width(30f),
//                        FontVariation.slant(-6f),
//                    )
//                )
//            ),
                style = TextStyle(
                    fontWeight = FontWeight(1000), textIndent = TextIndent(), shadow = Shadow(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        offset = Offset(15f, 8f),
                        blurRadius = 10f

                    ), color = Color.White
                )
            )
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    )
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    model = state.myProfile,
                    contentDescription = "profile",
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp)
            ) { page ->
                Box(modifier = Modifier) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 10.dp
                            )
                            .graphicsLayer {
                                // Calculate the absolute offset for the current page from the
                                // scroll position. We use the absolute value which allows us to mirror
                                // any effects for both directions
                                val pageOffset =
                                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                                // We animate the alpha, between 50% and 100%
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }, elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        WeatherItem(
                            position = page,
                            pagerState = pagerState.currentPage,
                            info = state.ffLocations
                        )
                    }
                    when (state.ffLocations[page].weather) {
                        "Rain" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.raining
                            )
                        }

                        "Snow" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.snow
                            )
                        }

                        "Clouds" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.cloudy
                            )
                        }

                        "Atmosphere" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.sunny
                            )
                        }

                        "Clear" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.sunny
                            )
                        }

                        "Drizzle" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.drizzle
                            )
                        }

                        "Thunderstorm" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.thunder
                            )
                        }

                        "Mist" -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.mist
                            )
                        }

                        else -> {
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.windy
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 20.dp, bottom = 10.dp),
                        shape = CircleShape
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(50.dp),
                            model = state.ffLocations[page].userProfile,
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )
                    }

                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            state.stories.forEach { item ->
                Box {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = item.imageUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 15.dp, top = 10.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(onClick = {
                            navController.navigate(
                                Destination.Screen.CommentDestination.route.replace(
                                    "{img_id}", item.time + "@" + item.userId
                                )
                            )
                        }) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = Icons.Filled.Comment,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = ""
                            )
                        }
                        if (item.cmtUserProfile.isNotBlank() && item.cmtUserProfile != state.myProfile) {
                            Card(
                                modifier = Modifier,
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                elevation = CardDefaults.cardElevation(10.dp)
                            ) {
                                AsyncImage(
                                    modifier = Modifier.size(40.dp),
                                    model = item.cmtUserProfile,
                                    contentDescription = ""
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

            }

        }


    }
}

@Composable
fun WeatherItem(position: Int, pagerState: Int, info: List<UpdateStatusRequestResponse>) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .padding(vertical = if (pagerState == position) 15.dp else 1.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = info[position].temperature, style = TextStyle(
                    fontWeight = FontWeight.W900,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        offset = Offset(15f, 8f),
                        blurRadius = 10f

                    )
                )
            )
            Text(
                text = "℃", style = TextStyle(
                    fontWeight = FontWeight.W900, fontSize = 25.sp,

                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        offset = Offset(15f, 8f),
                        blurRadius = 10f

                    )
                )
            )
            Icon(
                modifier = Modifier.padding(start = 5.dp),
                imageVector = Icons.Filled.BatteryAlert, contentDescription = "")
            Text( modifier = Modifier.padding(end = 5.dp),text = info[position].batteryLife)
            when(info[position].audioProfile){
                "0" ->{
                    Icon(imageVector = Icons.Filled.MusicOff, contentDescription = "")
                }
                "1" ->{
                    Icon(imageVector = Icons.Filled.Vibration, contentDescription = "")

                }
                "2" ->{
                    Icon(imageVector = Icons.Filled.MusicNote, contentDescription = "")

                }
                else ->{

                }
            }


        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(info[position].ress + ",")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 32.sp, fontWeight = FontWeight(1000)
                    )
                ) {
                    append(info[position].country)
                }
            })
        }
        Text(
            text = "Synced " + MyDate.differenceOfDates(
                info[position].date, System.currentTimeMillis().toString()
            ),
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 3.dp),
                text = info[position].weather
            )
        }
    }

}