package com.chetan.ff.data.model

data class PushNotificationRequest(
    val app_id :String = "cd46103b-b29e-4076-91d0-b40b05738860",
//    val include_subscription_ids : List<String> = listOf(""),
    val include_player_ids : List<String> = emptyList(),
    val included_segments: List<String> = emptyList(),
    val contents: Map<String, String>,
    val headings: Map<String, String>
)