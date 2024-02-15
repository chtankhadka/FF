package com.chetan.ff.presentation.musicplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
class MusicPlayerViewModel @Inject constructor(
    private val audioServiceHandler: AudioServiceHandler,
    private val repository: AudioRepository,
    private val preference: Preference
) : ViewModel() {
    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state

    init {
        loadAudioData()

    }

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest { mediaState ->

                when (mediaState) {
                    is AudioState.Buffering ->{
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
                                isPlaying = mediaState.isPlaying
                            )
                        }
                    }

                    is AudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is AudioState.Ready -> {
                        _state.update {
                            it.copy(
                                duration = mediaState.duration,
                                uiState = UIState.Ready
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
            setMediaItems()
        }
    }

    private fun setMediaItems() {
        state.value.audioList.map { audio ->
            MediaItem.Builder()
                .setUri(audio.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audio.artist)
                        .setDisplayTitle(audio.title)
                        .setSubtitle(audio.displayName)
                        .build()
                )
                .build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgressValue(currentProgress: Long) {
        _state.update {
            it.copy(
                progress =
                if (currentProgress > 0) ((currentProgress.toFloat() / state.value.duration.toFloat()) * 100f) else 0f,
                progressString = formatDuration(currentProgress)
            )
        }

    }

    fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val second = (minute) - minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
        return String.format("%02d:&02d", minute, second)
    }

    val onEvent: (event: MusicPlayerEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                MusicPlayerEvent.Backward -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
                }


                MusicPlayerEvent.Forward -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
                }

                MusicPlayerEvent.PlayPause -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.PlayPause
                    )
                    _state.update {
                        it.copy(
                            isPlaying = !state.value.isPlaying
                        )
                    }
                }

                is MusicPlayerEvent.SeekTo -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SeekTo,
                        seekPosition = ((state.value.duration * event.position) / 100f).toLong()
                    )
                }

                MusicPlayerEvent.SeekToNext -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
                }

                MusicPlayerEvent.SeekToBack -> {
                    audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToBack)
                }

                is MusicPlayerEvent.SelectedAudioChange -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.SelectedAudioChange,
                        selectedAudioIndex = event.index
                    )
                }

                is MusicPlayerEvent.UpdateProgress -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.UpdateProgress(event.newProgress)
                    )
                }
            }
        }

    }

}
