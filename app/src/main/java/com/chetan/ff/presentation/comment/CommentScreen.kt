package com.chetan.ff.presentation.comment

import android.widget.RemoteViews.RemoteView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun CommentScreen(
    state: CommentState,
    onEvent: (onEvent: CommentEvent) -> Unit,
    id: List<String>,
    nav: NavHostController
) {
    state.imgId.ifBlank {
        onEvent(CommentEvent.GetCmtHistories(id.first()))
    }
    BackHandler {
       if (state.newMsgSent && state.cmtList.last().item.cmtUser != state.tableName){
           onEvent(CommentEvent.UpdateStories(id.last()))
       }
        nav.popBackStack()

    }
    Scaffold(content = {
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
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(if (state.userName == data.item.cmtUser) Alignment.CenterEnd else Alignment.CenterStart),
                                elevation = CardDefaults.cardElevation(10.dp),
                                colors = if (state.userName == data.item.cmtUser) {
                                    CardDefaults.cardColors(
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                } else {
                                    CardDefaults.cardColors()
                                }

                            ) {
                                var showTime by remember {
                                    mutableStateOf(false)
                                }
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() } // This is mandatory
                                    ) {
                                        showTime = !showTime
                                    }
                                ) {
                                    if (showTime) {
                                        Text(
                                            text = data.item.time,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        )
                                    }

                                    Text(
                                        text = data.item.cmtUser,
                                        color = MaterialTheme.colorScheme.outline,
                                        fontSize = 10.sp

                                    )
                                    Text(
                                        text = data.item.cmt,
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

@Composable
@Preview
fun view(){

        val inputString = "h--=-0nep -- ho ello@#$%^&*("

        // Use regex to remove special characters
        val resultString = inputString.replace(Regex("[^A-Za-z0-9 ]"), "")

    Text(text = resultString)
        println("Original String: $inputString")
        println("Result String: $resultString")
    
}