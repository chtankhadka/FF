package com.chetan.ff.presentation.musicplayer

import android.net.Uri
import com.chetan.ff.data.local.model.Audio

sealed class UIState {
    data object Initial : UIState()
    data object Ready : UIState()

}
data class MusicPlayerState(
    val duration: Long = 0L,
    val audioIndex: Int = 0,

    val progress: Float = 0f,
    val progressString: String = "00:00",
    val isPlaying: Boolean = false,

    val audioList: List<Audio> = emptyList(),
    val currentSelectedAudio: Audio = Audio(Uri.EMPTY,"",0L,"","",0,""),
    val uiState: UIState = UIState.Initial
)
