package com.chetan.ff.presentation.dashboard.library

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.ff.Destination
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    navController: NavHostController,
    event: (event: LibraryEvent) -> Unit,
    state: LibraryState,
    onStart: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        Icons.Default.PlaylistPlay,
        Icons.Default.EventRepeat,
        Icons.Default.Favorite
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .animateContentSize(),
    ) {
        val pagerState = rememberPagerState {
            4
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Songs", fontSize = 50.sp, fontFamily = FontFamily.Default,
                style = TextStyle(
                    fontWeight = FontWeight(1000), textIndent = TextIndent(), shadow = Shadow(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        offset = Offset(15f, 8f),
                        blurRadius = 10f

                    ), color = Color.White
                )
            )
            Card(
                modifier = Modifier.padding(end = 10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    IconButton(onClick = {
                        navController.navigate(Destination.Screen.MusicPlayerDestination.route) {}
                    }) {
                        Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "Play")
                    }
                    IconButton(onClick = {
                        event(LibraryEvent.PlayPause)
                        onStart()
                    }) {
                        Icon(
                            imageVector = if (state.isPlaying) Icons.Default.PauseCircleOutline else Icons.Default.PlayCircleOutline,
                            contentDescription = "Play"
                        )
                    }
                    IconButton(onClick = {
                        event(LibraryEvent.SeekToNext)
                    }) {
                        Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Next")
                    }
                }
            }

        }

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                tabs.forEachIndexed { index, icon ->
                    Card(
                        modifier = Modifier.size(if (index == selectedTabIndex) 50.dp else 40.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        IconButton(
                            onClick = {
                                selectedTabIndex = index
                            }) {
                            Icon(
                                tint = if (index == selectedTabIndex) Color.White else MaterialTheme.colorScheme.outline,
                                imageVector = icon, contentDescription = ""
                            )
                        }
                    }

                }
                when (selectedTabIndex) {
                    0 -> {

                    }

                    1 -> {

                    }

                    2 -> {

                    }
                }

            }
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                HorizontalPager(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, end = 20.dp),
                    state = pagerState,
                    pageSize = PageSize.Fixed(200.dp)
                ) { page ->
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
                        AlbumItem(position = page, pagerState = pagerState.currentPage)
                    }
                }
                state.audioList.forEachIndexed { index, audio ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                event(LibraryEvent.SelectedAudioChange(index))
                                if (!state.isPlaying){
                                    onStart()
                                }
                            }
                            .background(
                                if (audio.id == state.currentSelectedAudio.id) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(modifier = Modifier.size(65.dp)) {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = audio.albumArtUri, contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = audio.displayName,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = audio.artist,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.outline,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert, contentDescription = "",
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
        }

    }
}

@Composable
fun AlbumItem(position: Int, pagerState: Int) {
    val listEg = listOf(
        "https://m.media-amazon.com/images/I/61w8iMIL8YL._AC_UF894,1000_QL80_.jpg",
        "https://upload.wikimedia.org/wikipedia/en/thumb/4/47/Iron_Man_%28circa_2018%29.png/220px-Iron_Man_%28circa_2018%29.png",
        "https://upload.wikimedia.org/wikipedia/en/4/4a/Iron_Man_Mark_III_armor_from_Iron_Man_%282008_film%29.jpg",
        "https://static.independent.co.uk/s3fs-public/thumbnails/image/2008/04/30/21/26206.jpg"
    )
    Box() {
        Card(
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            AsyncImage(
                modifier = Modifier.height(if (position == pagerState) 250.dp else 200.dp),
                model = listEg[pagerState],
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }
    }

}


