package com.teamtriad.forpets.data.source.network

import com.teamtriad.forpets.data.source.network.model.KeyResponse
import com.teamtriad.forpets.data.source.network.model.TransportReq
import retrofit2.Response
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
}