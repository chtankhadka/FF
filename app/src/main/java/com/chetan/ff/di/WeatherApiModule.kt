package com.chetan.ff.di

import com.chetan.ff.data.repositoryImpl.WeatherRepositoryImpl
import com.chetan.ff.domain.repository.WeatherRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object WeatherApiModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient{
        val interceptors = HttpLoggingInterceptor()
        interceptors.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptors).build()
    }

    @Provides
    fun provideGson(): Gson{
        return GsonBuilder()
            .create()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideWeatherApiService(retrofit: Retrofit) : WeatherRepository{
        return retrofit.create(WeatherRepository::class.java)
    }
    @Provides
    fun provideWeatherRepository(repository: WeatherRepository) : WeatherRepositoryImpl{
        return WeatherRepositoryImpl(repository)
    }

}