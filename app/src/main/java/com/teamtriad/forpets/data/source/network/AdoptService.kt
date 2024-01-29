package com.teamtriad.forpets.data.source.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AdoptService {

    @GET(
        "1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=${BuildConfig.ABANDONMENT_API_KEY}" +
                "&state=protect&_type=json"
    )
    suspend fun getAbandonmentPublic(
        @Query("bgnde") bgnde: String,
        @Query("endde") endde: String,
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String = "30",
    ): Response<AbandonmentPublic>

    companion object {

        fun getService(): AdoptService = Retrofit.Builder()
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