package com.teamtriad.forpets

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.BuildConfig.REMOTE_DATABASE_BASE_URL
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
    }
}