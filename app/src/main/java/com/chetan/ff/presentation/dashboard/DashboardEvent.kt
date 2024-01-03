package com.chetan.ff.presentation.dashboard

import android.net.Uri

sealed interface DashboardEvent {
    data class UploadImage(val value: Uri): DashboardEvent
    data object DismissInfoMsg: DashboardEvent


}