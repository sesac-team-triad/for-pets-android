package com.teamtriad.forpets.data.source.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.BuildConfig
import com.teamtriad.forpets.data.source.network.model.KeyResponse
import com.teamtriad.forpets.data.source.network.model.TransportReq
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteDatabaseService {

    @POST("transport-req.json")
    suspend fun addTransportReq(
        @Body transportReq: TransportReq
    ): Response<KeyResponse>

    @GET("transport-req.json")
    suspend fun getAllTransportReqMap(): Response<Map<String, TransportReq>>

    @GET("transport-req/{key}.json")
    suspend fun getTransportReqByKey(
        @Path("key") key: String
    ): Response<TransportReq>

    companion object {

        fun getService(): RemoteDatabaseService = Retrofit.Builder()    // TODO: 인스턴스 하나만 생기도록 변경하기.
            .baseUrl(BuildConfig.REMOTE_DATABASE_BASE_URL)
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