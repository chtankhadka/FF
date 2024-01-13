package com.chetan.ff.di

import com.chetan.ff.data.repositoryImpl.OneSignalRepository
import com.chetan.ff.data.repositoryImpl.WeatherRepositoryImpl
import com.chetan.ff.domain.repository.OneSignalRepositoryImpl
import com.chetan.ff.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideWeatherApiService(@Named("weather") retrofit: Retrofit): WeatherRepository {
        return retrofit.create(WeatherRepository::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(repository: WeatherRepository): WeatherRepositoryImpl {
        return WeatherRepositoryImpl(repository)
    }

    @Provides
    @Singleton
    fun provideOneSignalApiService(@Named("oneSignal") retrofit: Retrofit): OneSignalRepository {
        return retrofit.create(OneSignalRepository::class.java)
    }

    @Provides
    @Singleton
    fun provideOneSignalRepository(repository: OneSignalRepository): OneSignalRepositoryImpl {
        return OneSignalRepositoryImpl(repository)
    }
}