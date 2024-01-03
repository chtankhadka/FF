package com.chetan.ff.domain.use_cases.firestore

data class FirestoreUseCases(
    val updateStatus: UpdateStatus,
    val getStatus: GetStatus,
    val getStories: GetStories,
    val setStories: SetStories,
    val updateCommentedUserInStories: UpdateCommentedUserInStories
)
