package com.chetan.ff.presentation.dashboard

import com.chetan.ff.presentation.dialogs.Message
import com.chetan.ff.utils.FFScreenState

data class DashboardState(
    val test: String = "",
    override val infoMsg: Message? = null
) : FFScreenState(infoMsg)

