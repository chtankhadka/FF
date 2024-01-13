package com.chetan.ff.di

import com.chetan.ff.data.repositoryImpl.WeatherRepositoryImpl
import com.chetan.ff.domain.use_cases.firestore.FirestoreUseCases
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface HiltEntryPoint {
    fun firestoreUseCases() : FirestoreUseCases
    fun weatherRepository(): WeatherRepositoryImpl
}