package com.chetan.ff.presentation.musicplayer

sealed interface MusicPlayerEvent {
    data object PlayPause : MusicPlayerEvent
    data class SelectedAudioChange(val index: Int) : MusicPlayerEvent
    data class SeekTo(val position: Float) : MusicPlayerEvent
    data object SeekToNext : MusicPlayerEvent
    data object Backward : MusicPlayerEvent
    data object SeekToBack : MusicPlayerEvent
    data object Forward : MusicPlayerEvent
    data class UpdateProgress(val newProgress: Float) : MusicPlayerEvent
}