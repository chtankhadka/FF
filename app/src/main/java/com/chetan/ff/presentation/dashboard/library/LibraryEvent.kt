package com.chetan.ff.presentation.dashboard.library

sealed interface LibraryEvent {
    data object PlayPause : LibraryEvent
    data class SelectedAudioChange(val index: Int) : LibraryEvent
    data class SeekTo(val position: Float) : LibraryEvent
    data object SeekToNext : LibraryEvent
    data object Backward : LibraryEvent
    data object SeekToBack : LibraryEvent
    data object Forward : LibraryEvent
    data class UpdateProgress(val newProgress: Float) : LibraryEvent
}