@file:OptIn(ExperimentalFoundationApi::class)

package com.chetan.ff.presentation.ourlocations

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.ff.R
import com.chetan.ff.utils.RequestPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@SuppressLint("MissingPermission")
@Composable
fun OurLocationsScreen(nav: NavHostController, state: OurLocationsState) {


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var hideDialog by remember {
        mutableStateOf(false)
    }

    var locationInfo by remember {
        mutableStateOf("")
    }
    var canOrder by remember {
        mutableStateOf(false)
    }
    if (canOrder) {
        if (!isGpsEnabled && !hideDialog) {
            AlertDialog(title = {
                Text(
                    text = "Enable GPS", style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            }, text = {
                Text("Please enable GPS to use this app.")
            }, onDismissRequest = { }, confirmButton = {
                Button(onClick = {
                    // Redirect the user to GPS settings
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                    hideDialog = true
                }) {
                    Text("Enable GPS")
                }
            })
        } else {
            LaunchedEffect(key1 = canOrder, block = {
                scope.launch(Dispatchers.IO) {
                    val priority = Priority.PRIORITY_HIGH_ACCURACY
                    val result =
                        locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                            .await()
                    result?.let { fetchedLocation ->
                        locationInfo = "${fetchedLocation.latitude},${fetchedLocation.longitude}"
                    }
                }
            })
        }
    }
    val cameraPositionState = com.google.maps.android.compose.rememberCameraPositionState()

    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION) {
        canOrder = it
    }


    LaunchedEffect(key1 = locationInfo, block = {
        if (locationInfo.isNotBlank()) {
            val userlatlng = locationInfo.split(",")
            val position = CameraPosition.fromLatLngZoom(
                LatLng(
                    userlatlng.first().toDouble(), userlatlng.last().toDouble()
                ), 16f
            )
            cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(position),7000)
            cameraPositionState.position = position
        }

    })


    val userCardItemSize = (LocalConfiguration.current.screenWidthDp / 5)
    val lazyListState = rememberLazyListState()
    val snapBehaviour = rememberSnapFlingBehavior(lazyListState = lazyListState)


    var selectedItem by remember {
        mutableIntStateOf(3)
    }

    LaunchedEffect(key1 = selectedItem) {
        if (state.ffusers.isNotEmpty()){
            delay(1000)

            println(selectedItem)
            if (state.ffusers[selectedItem].locationLat.isNotBlank() && state.ffusers[selectedItem].id != "all"){
                locationInfo = state.ffusers[selectedItem].locationLat + "," + state.ffusers[selectedItem].locationLng

            }

        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = remember {
                MapProperties(
                    mapType = MapType.SATELLITE,
                    isMyLocationEnabled = true,
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.map_style
                    )
                )
            },
            onMapClick = {

            },
            onMapLongClick = {

            }) {
            state.ffLocations.forEach { userData ->
                if (userData.locationLat.isNotBlank()){
                    Marker(state = rememberMarkerState(
                        position = LatLng(
                            userData.locationLat.toDouble(), userData.locationLng.toDouble()
                        ),
                    ),
                        draggable = false,
                        title = userData.googleUserName,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                        onInfoWindowLongClick = {
                            val uri =
                                Uri.parse("google.navigation:q=${userData.locationLat},${userData.locationLng}&origin=${locationInfo}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            // Start the navigation intent
                            context.startActivity(mapIntent)
                        },
                        onInfoWindowClick = {
//                        onEvent(AdminMapEvent.OnClickWindoInfo(userData.userMail))
//                        showUserDetails = true
                        },
                        onClick = {
                            it.showInfoWindow()
                            true
                        },
                        tag = userData.userProfile)
                }

            }

        }
        LazyRow(
            modifier = Modifier
                .height(150.dp)
                .align(Alignment.TopCenter),
            state = lazyListState,
            flingBehavior = snapBehaviour
        ) {
            itemsIndexed(state.ffusers) { index, item ->
                var size = userCardItemSize.dp
                var requiredOffset by remember {
                    mutableStateOf(IntOffset.Zero)
                }
                when (index) {
                    lazyListState.firstVisibleItemIndex + 1 -> {
                        requiredOffset = IntOffset(0, 20)
                        size += 10.dp
                    }

                    lazyListState.firstVisibleItemIndex + 2 -> {
                        requiredOffset = IntOffset(0, 50)
                        size += 20.dp

                        selectedItem = index
                    }

                    lazyListState.firstVisibleItemIndex + 3 -> {
                        requiredOffset = IntOffset(0, 20)
                        size += 10.dp
                    }

                    else -> {
                        requiredOffset = IntOffset.Zero
                        size
                    }
                }
                val offset by animateIntOffsetAsState(
                    targetValue = requiredOffset, label = "offset"
                )
                Card(
                    modifier = Modifier
                        .size(size)
                        .padding(5.dp)
                        .offset(offset.x.dp, offset.y.dp),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    shape = RoundedCornerShape(100)

                ) {
                    if (item.id != "all") {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {

                                },
                            model = item.userProfile,
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.all),
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { },
                            contentDescription = "all",
                            tint = Color.Unspecified
                        )
                    }

                }

            }
        }
        Text(text = selectedItem.toString())
    }

}