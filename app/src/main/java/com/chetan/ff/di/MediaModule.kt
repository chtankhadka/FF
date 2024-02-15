package com.chetan.ff.di

import android.content.ContentResolver
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.chetan.ff.data.local.ContentResolverHelper
import com.chetan.ff.data.repositoryImpl.AudioRepositoryImpl
import com.chetan.ff.domain.repository.AudioRepository
import com.chetan.ff.service.AudioNotificationManager
import com.chetan.ff.service.AudioServiceHandler
import com.chetan.orderdelivery.data.local.Preference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @Singleton
    @UnstableApi
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): AudioNotificationManager = AudioNotificationManager(context = context, exoPlayer = player)

    @Provides
    @Singleton
    fun provideServiceHandler(exoPlayer: ExoPlayer,preference: Preference): AudioServiceHandler =
        AudioServiceHandler(exoPlayer,preference)

    @Provides
    @Singleton
    fun provideAudioRepository(resolver: ContentResolverHelper): AudioRepository = AudioRepositoryImpl(resolver)
}