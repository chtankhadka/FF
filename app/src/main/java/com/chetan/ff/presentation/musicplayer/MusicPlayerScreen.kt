package com.chetan.ff.presentation.musicplayer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.ff.presentation.dashboard.library.LibraryEvent
import com.chetan.ff.presentation.dashboard.library.LibraryState
import kotlin.math.floor

data class Message(val text: String, val isFlagged: Boolean)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicPlayerScreen(
    nav: NavHostController,
    onStart: () -> Unit,
    event: (event: MusicPlayerEvent) -> Unit,
    state: MusicPlayerState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(bottom = 50.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        var showCmt by remember {
            mutableStateOf(false)
        }
        var msg by remember {
            mutableStateOf(
                listOf(
                    Message("oe k suneko be???", false),
                )
            )
        }
        var userMsg by remember {
            mutableStateOf("")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
            ) {
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                }
            }
            Text(
                text = "Now playing", fontSize = 30.sp, fontFamily = FontFamily.Default,
                style = TextStyle(
                    fontWeight = FontWeight(500), textIndent = TextIndent(), shadow = Shadow(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        offset = Offset(15f, 8f),
                        blurRadius = 10f

                    ), color = Color.White
                )
            )
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
            ) {
                IconButton(modifier = Modifier.size(40.dp), onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .animateContentSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (!showCmt) {
                Spacer(modifier = Modifier.height(15.dp))
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier.size(250.dp),
                        model = state.currentSelectedAudio.albumArtUri,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = state.currentSelectedAudio.displayName,
                            color = MaterialTheme.colorScheme.secondary,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = state.currentSelectedAudio.artist,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.outline,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Favorite, contentDescription = "",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            //progress bar
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = state.progress,
                onValueChange = {
                    event(MusicPlayerEvent.SeekTo(it))
                },
                valueRange = 0f..100f)

            //time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = state.progressString, fontWeight = FontWeight.Bold)
                Text(text = timeStampToDuration(state.duration), fontWeight = FontWeight.Bold)

            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.outline,
                        imageVector = Icons.Default.Repeat, contentDescription = ""
                    )
                }
                IconButton(onClick = {
                    event(MusicPlayerEvent.SeekToBack)
                }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = ""
                    )
                }
                Card(
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                ) {
                    IconButton(onClick = {
                        event(MusicPlayerEvent.PlayPause)
                        onStart()
                    }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = if(state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = "Pause"
                        )
                    }
                }

                IconButton(onClick = {
                    event(MusicPlayerEvent.SeekToNext)
                }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.SkipNext, contentDescription = ""
                    )
                }
                IconButton(onClick = {
                    showCmt = !showCmt
                }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.outline,
                        imageVector = if (showCmt) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = ""
                    )
                }
            }

            // Chat or Cmt
            if (showCmt) {
                msg.forEach { data ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .align(if (data.isFlagged) Alignment.CenterEnd else Alignment.CenterStart),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors =
                            if (data.isFlagged) {
                                CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            } else {
                                CardDefaults.cardColors()
                            }

                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text(
                                        text = data.text,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "2023/12/30",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                                Text(
                                    text = data.text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                        }
                    }

                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(), // Fill maximum width
                    value = userMsg,
                    placeholder = {
                        Text(text = "Write a comment...")
                    },
                    trailingIcon = {
                        if (userMsg.isNotBlank()) {
                            IconButton(onClick = {
                                val newItem = Message(userMsg, true)
                                msg = msg.toMutableList().also { it.add(newItem) }
                                userMsg = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send Comment"
                                )
                            }
                        }
                    },
                    onValueChange = {
                        userMsg = it
                    }
                )

            }

        }
        if (showCmt){

        }


    }
}
private fun timeStampToDuration(position: Long): String{
    val totalSecond = floor(position /1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
}