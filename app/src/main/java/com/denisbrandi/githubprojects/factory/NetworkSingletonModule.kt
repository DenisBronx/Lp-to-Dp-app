package com.denisbrandi.githubprojects.factory

import com.squareup.moshi.Moshi
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkSingletonModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .client(OkHttpClient())
            .baseUrl("https://api.github.com/")
            .build()
    }

}