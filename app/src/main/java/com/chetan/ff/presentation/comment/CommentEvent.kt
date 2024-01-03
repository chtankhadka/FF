package com.chetan.ff.presentation.comment

sealed interface CommentEvent {
    data object SetChatHistory: CommentEvent
    data class OnMsgChange(val value: String): CommentEvent
}