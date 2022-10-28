package com.denisbrandi.factory

import android.content.Context
import com.ashokvarma.gander.GanderInterceptor
import com.squareup.moshi.Moshi
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkSingletonModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext context: Context
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(GanderInterceptor(context).apply { showNotification(true) })
            .callTimeout(3L, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .client(client)
            .baseUrl("https://api.github.com/")
            .build()
    }

}