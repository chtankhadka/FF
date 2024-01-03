package com.chetan.ff.presentation.comment

sealed interface CommentEvent {
    data object SetChatHistory: CommentEvent
    data class OnMsgChange(val value: String): CommentEvent
    data class GetCmtHistories(val imgId: String) : CommentEvent
    data class UpdateStories(val value: String) : CommentEvent
}