package com.chetan.ff.domain.repository

import com.chetan.ff.data.local.model.Audio

interface AudioRepository {
    suspend fun getAudioData(): List<Audio>
}