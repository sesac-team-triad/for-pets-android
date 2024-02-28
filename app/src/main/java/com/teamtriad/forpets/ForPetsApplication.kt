package com.teamtriad.forpets

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.BuildConfig.REMOTE_DATABASE_BASE_URL
import com.teamtriad.forpets.data.source.network.AdoptService
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ForPetsApplication : Application() {

    companion object {

        val remoteDatabaseService: RemoteDatabaseService by lazy {
            Retrofit.Builder()
                .baseUrl(REMOTE_DATABASE_BASE_URL)
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                    )
                )
                .build()
                .create(RemoteDatabaseService::class.java)
        }

        val adoptService: AdoptService by lazy {
            Retrofit.Builder()
                .baseUrl("https://apis.data.go.kr/")
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                    )
                )
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build()
                )
                .build()
                .create(AdoptService::class.java)
        }
    }
}