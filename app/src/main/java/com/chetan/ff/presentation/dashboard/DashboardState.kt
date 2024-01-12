package com.chetan.ff.presentation.dashboard

import com.chetan.ff.data.model.SetGetGroupsName
import com.chetan.ff.presentation.dialogs.Message
import com.chetan.ff.utils.FFScreenState

data class DashboardState(
    val groupName: String = "",
    val onChangeGroupName:String = "",
    val groupList: List<SetGetGroupsName> = emptyList(),
    override val infoMsg: Message? = null
) : FFScreenState(infoMsg)

