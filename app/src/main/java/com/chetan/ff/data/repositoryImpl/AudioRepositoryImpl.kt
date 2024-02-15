package com.chetan.ff.data.repositoryImpl

import com.chetan.ff.data.local.ContentResolverHelper
import com.chetan.ff.data.local.model.Audio
import com.chetan.ff.domain.repository.AudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolverHelper
): AudioRepository {
    override suspend fun getAudioData(): List<Audio> {
        return withContext(Dispatchers.IO){contentResolver.getAudioData()}
    }
}