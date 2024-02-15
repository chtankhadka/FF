package com.chetan.ff.presentation.dashboard.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import com.chetan.ff.domain.repository.AudioRepository
import com.chetan.ff.service.AudioServiceHandler
import com.chetan.ff.service.AudioState
import com.chetan.ff.service.PlayerEvent
import com.chetan.orderdelivery.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val audioServiceHandler: AudioServiceHandler,
    private val repository: AudioRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {
    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state

    init {
        loadAudioData()
        _state.update {
            it.copy(
                audioIndex = exoPlayer.currentMediaItemIndex,
                isPlaying = exoPlayer.isPlaying)
        }

    }

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    is AudioState.Buffering -> {
                        calculateProgressValue(mediaState.progress)
                    }

                    is AudioState.CurrentPlaying -> {
                        _state.update {
                            it.copy(
                                currentSelectedAudio = _state.value.audioList[mediaState.mediaItemIndex]
                            )
                        }
                    }

                    AudioState.Initial -> {
                        _state.update {
                            it.copy(
                                uiState = UIState.Initial
                            )
                        }
                    }

                    is AudioState.Playing -> {
                        _state.update {
                            it.copy(
                                isPlaying = exoPlayer.isPlaying,
                                currentSelectedAudio = _state.value.audioList[exoPlayer.currentMediaItemIndex]
                            )
                        }
                    }

                    is AudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is AudioState.Ready -> {
                        _state.update {
                            it.copy(
                                duration = mediaState.duration, uiState = UIState.Ready
                            )
                        }

                    }
                }
            }
        }
    }

    private fun loadAudioData() {
        viewModelScope.launch {
            val audio = repository.getAudioData()
            _state.update {
                it.copy(
                    audioList = audio
                )
            }
            if (!exoPlayer.isPlaying){
                setMediaItems()
            }

        }
    }

    private fun setMediaItems() {
        state.value.audioList.map { audio ->
            MediaItem.Builder().setUri(audio.uri).setMediaMetadata(
                    MediaMetadata.Builder().setAlbumArtist(audio.artist)
                        .setDisplayTitle(audio.title).setSubtitle(audio.displayName).build()
                ).build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        _state.update {
            it.copy(
                progress = if (currentProgress > 0) ((currentProgress.toFloat() / state.value.duration.toFloat()) * 100f) else 0f,
                progressString = formatDuration(currentProgress)
            )
        }

    }

    fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val second = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format("%02d:%02d", minute, second)
    }

    val onEvent: (event: LibraryEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                LibraryEvent.Backward -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
                }

                LibraryEvent.Forward -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
                }

                LibraryEvent.PlayPause -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.PlayPause
                    )

                }

                is LibraryEvent.SeekTo -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SeekTo,
                        seekPosition = ((state.value.duration * event.position) / 100f).toLong()
                    )
                }

                LibraryEvent.SeekToNext -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
                    _state.update {
                        it.copy(
                            audioIndex = exoPlayer.currentMediaItemIndex
                        )
                    }
                }

                LibraryEvent.SeekToBack -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToBack)
                    _state.update {
                        it.copy(
                            audioIndex = exoPlayer.currentMediaItemIndex
                        )
                    }
                }

                is LibraryEvent.SelectedAudioChange -> {
                    _state.update {
                        it.copy(
                            audioIndex = event.index
                        )
                    }
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SelectedAudioChange, selectedAudioIndex = event.index
                    )

                }

                is LibraryEvent.UpdateProgress -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.UpdateProgress(event.newProgress)
                    )
                    _state.update {
                        it.copy(
                            progress = event.newProgress
                        )
                    }
                }
            }
        }

    }


}
