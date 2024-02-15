package com.chetan.ff.service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.chetan.orderdelivery.data.local.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val preference: Preference
) : Player.Listener {
    private val _audioState: MutableStateFlow<AudioState> = MutableStateFlow(AudioState.Initial)
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

    private var job: Job? = null
    init {
        exoPlayer.addListener(this)
    }

    fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0
    ) {
        when (playerEvent) {
            PlayerEvent.Backward -> exoPlayer.seekBack()
            PlayerEvent.Forward -> exoPlayer.seekForward()
            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            PlayerEvent.SeekToNext -> exoPlayer.seekToNext()
            PlayerEvent.SeekToBack -> exoPlayer.seekToPrevious()
            PlayerEvent.SelectedAudioChange -> {
                when (selectedAudioIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playOrPause()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                        preference.audioIndex = selectedAudioIndex
                        _audioState.value = AudioState.Playing(
                            isPlaying = true
                        )
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                AudioState.Buffering(exoPlayer.currentPosition)
            ExoPlayer.STATE_READY -> _audioState.value =
                AudioState.Ready(exoPlayer.duration)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = AudioState.Playing(isPlaying = isPlaying)
        _audioState.value = AudioState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
        if (isPlaying){
            GlobalScope.launch(Dispatchers.Main) {
                stopProgressUpdate()
            }
        }else{
            stopProgressUpdate()
        }
    }
    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = AudioState.Playing(
                isPlaying = true
            )
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _audioState.value = AudioState.Progress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = AudioState.Playing(isPlaying = false)
    }

}

sealed class PlayerEvent {
    data object PlayPause : PlayerEvent()
    data object SelectedAudioChange : PlayerEvent()
    data object Backward : PlayerEvent()
    data object SeekToNext : PlayerEvent()
    data object SeekToBack : PlayerEvent()
    data object Forward : PlayerEvent()
    data object SeekTo : PlayerEvent()
    data object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()

}

sealed class AudioState {
    data object Initial : AudioState()
    data class Ready(val duration: Long) : AudioState()
    data class Progress(val progress: Long) : AudioState()
    data class Buffering(val progress: Long) : AudioState()
    data class Playing(val isPlaying: Boolean) : AudioState()
    data class CurrentPlaying(val mediaItemIndex: Int) : AudioState()
}