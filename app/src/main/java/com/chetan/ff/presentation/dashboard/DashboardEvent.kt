package com.chetan.ff.presentation.dashboard

import android.net.Uri

sealed interface DashboardEvent {
    data class UploadImage(val value: Uri): DashboardEvent
    data object DismissInfoMsg: DashboardEvent
    data class OnGroupNameChange(val value: String) : DashboardEvent
    data class SetGroupName(val value: String) : DashboardEvent


}