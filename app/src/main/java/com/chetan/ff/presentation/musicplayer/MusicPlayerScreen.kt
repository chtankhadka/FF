package com.chetan.ff.presentation.musicplayer

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

data class Message(val text: String, val isFlagged: Boolean)
@Composable
fun MusicPlayerScreen(nav: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(bottom = 50.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        var slider by remember {
            mutableFloatStateOf(0.6f)
        }
        var showCmt by remember {
            mutableStateOf(false)
        }
        var msg by remember {
            mutableStateOf(
                listOf(
                    Message("oe k suneko be???", false),
                    Message("tero tauko suneko .. haha", true)
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
                    modifier = Modifier
                        .size(250.dp)
                        .padding(3.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(20.dp)
                ) {
                    AsyncImage(
                        model = "https://pics.craiyon.com/2023-12-02/jDNJyPzdSMKQP2yHzfQ_VQ.webp",
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("Hello good")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        ) {
                            append("\nGood again bro")
                        }
                    })
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
                value = slider,
                onValueChange = {
                    slider = it
                })

            //time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "1:42", fontWeight = FontWeight.Bold)
                Text(text = "3:29", fontWeight = FontWeight.Bold)

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
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = ""
                    )
                }
                Card(
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.Pause, contentDescription = "Pause"
                        )
                    }
                }

                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.FastForward, contentDescription = ""
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