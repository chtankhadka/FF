package com.chetan.ff.presentation.dashboard

import android.net.Uri
import com.chetan.ff.data.model.RequestGroupDeatails

sealed interface DashboardEvent {
    data class UploadImage(val value: Uri): DashboardEvent
    data object DismissInfoMsg: DashboardEvent
    data class ChangeMyGroup(val value: String): DashboardEvent
    data class OnGroupNameChange(val value: String) : DashboardEvent
    data class OnChangeAdminGmail(val value: String) : DashboardEvent
    data class SetGroupName(val value: String) : DashboardEvent
    data class AcceptGroupRequest(val data: RequestGroupDeatails) : DashboardEvent
    data class SendGroupRequest(val groupAdmin : String, val groupName: String) : DashboardEvent


}