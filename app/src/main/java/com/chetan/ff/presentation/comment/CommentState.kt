package com.chetan.ff.presentation.comment

import com.chetan.ff.data.model.RealtimeModelResponse

data class CommentState(
    val userName: String = "",
    val tableName: String = "",
    val cmtList: List<RealtimeModelResponse> = emptyList(),
    val userMsg: String = "",
    val imgId: String = "",
    val newMsgSent : Boolean = false
    )

