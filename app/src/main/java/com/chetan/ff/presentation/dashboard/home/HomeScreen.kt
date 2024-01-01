package com.chetan.ff.presentation.dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
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
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.LocationOn
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
    var hide by remember {
        mutableStateOf(false)
    }

    //location
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
        val listEg = listOf(
            "https://m.media-amazon.com/images/I/61w8iMIL8YL._AC_UF894,1000_QL80_.jpg",
            "https://upload.wikimedia.org/wikipedia/en/thumb/4/47/Iron_Man_%28circa_2018%29.png/220px-Iron_Man_%28circa_2018%29.png",
            "https://upload.wikimedia.org/wikipedia/en/4/4a/Iron_Man_Mark_III_armor_from_Iron_Man_%282008_film%29.jpg",
            "https://static.independent.co.uk/s3fs-public/thumbnails/image/2008/04/30/21/26206.jpg"
        )
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
                text = "Family", fontSize = 50.sp, fontFamily = FontFamily.Default,
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
                    model = "https://scontent-lhr6-2.xx.fbcdn.net/v/t39.30808-1/343569459_1275463096718723_7844695765570199408_n.jpg?stp=c15.0.60.60a_cp0_dst-jpg_p60x60&_nc_cat=104&ccb=1-7&_nc_sid=5740b7&_nc_ohc=Dz2VvImVvEMAX8_1NXQ&_nc_ht=scontent-lhr6-2.xx&oh=00_AfCzqoIeMwXi3huse57vMTBTbQSquEm-ddxrT0GXMSC0KQ&oe=6593372A",
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
                    when(state.ffLocations[page].weather){
                        "Rain" ->{
                            LoadLottieAnimation(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.TopEnd), image = R.raw.raining
                        )}
                        "Snow" ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.snow
                            )
                        }
                        "Clouds" ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.cloudy
                            )
                        }
                        "Atmosphere" ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.sunny
                            )
                        }
                        "Clear" ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.sunny
                            )
                        }
                        "Drizzle" ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.sunny
                            )
                        }
                        "Thunderstorm" ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.thunder
                            )
                        }
                        else ->{
                            LoadLottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.TopEnd), image = R.raw.windy
                            )
                        }
                    }
                }


            }
            Spacer(modifier = Modifier.height(10.dp))
            listEg.forEach { item ->
                Box {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = item,
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
                        IconButton(onClick = {}) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = Icons.Filled.Comment,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = ""
                            )
                        }

                        listOf("CK", "RK", "CK").forEach {
                            Card(
                                modifier = Modifier,
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                elevation = CardDefaults.cardElevation(10.dp)
                            ) {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(3.dp),
                                    style = TextStyle(fontWeight = FontWeight.W900)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                        }
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
                    append(info[position].address + ",")
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