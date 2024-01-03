package com.chetan.ff.presentation.comment

import com.chetan.ff.data.model.CommentRequestResponse

data class CommentState(
    val userName: String = "",

    val cmtList: List<CommentRequestResponse> = listOf(
        CommentRequestResponse(
            msgId = "kkjk",
            cmt = "kkkj;"
        ),
        CommentRequestResponse(
            msgId = "kkjk",
            cmt = "kkkj;"
        ),
        CommentRequestResponse(
            msgId = "kkjk",
            cmt = "kkkj;"
        ),
        CommentRequestResponse(
            msgId = "kkjk",
            cmt = "kkkj;"
        )
    ),
    val userMsg: String = "",

)

