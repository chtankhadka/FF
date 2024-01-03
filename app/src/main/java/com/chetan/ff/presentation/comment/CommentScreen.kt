package com.chetan.ff.presentation.comment

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    nav: NavHostController,
    onBack: () -> Unit,
    state: CommentState,
    onEvent: (onEvent: CommentEvent) -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            Card(
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
            ) {
                IconButton(modifier = Modifier.size(40.dp), onClick = { onBack() }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                }
            }

        }, title = {

        })
    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(10.dp)
                .fillMaxHeight(), // Fill maximum height
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f) // Occupy available space
                    .fillMaxWidth() // Fill maximum width
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize(), reverseLayout = true, content = {
                    items(state.cmtList.reversed()) { data ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(if (state.userName == data.cmtUser) Alignment.CenterEnd else Alignment.CenterStart),
                                elevation = CardDefaults.cardElevation(10.dp),
                                colors = if (state.userName == data.cmtUser) {
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
                                            text = data.cmtUser,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Text(
//                                            text = MyDate.differenceOfDates(
//                                                data.time,
//                                                System.currentTimeMillis().toString()
//                                            ),
                                            text = "2232",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                    }
                                    Text(
                                        text = data.cmt,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                        }

                    }

                }

                )
            }
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), // Fill maximum width
                value = state.userMsg, placeholder = {
                    Text(text = "Write a comment...")
                }, trailingIcon = {
                    if (state.userMsg.isNotBlank()) {
                        IconButton(onClick = {
                            onEvent(CommentEvent.SetChatHistory)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Comment"
                            )
                        }
                    }
                }, onValueChange = {
                    onEvent(CommentEvent.OnMsgChange(it))
                })
        }

    })

}