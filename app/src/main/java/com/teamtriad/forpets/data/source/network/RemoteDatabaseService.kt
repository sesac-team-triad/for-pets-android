package com.teamtriad.forpets.data.source.network

import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.data.source.network.model.TransportVol
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RemoteDatabaseService {

    @POST("transport-req.json")
    suspend fun addTransportReq(
        @Body transportReq: TransportReq
    ): Response<Unit>

    @GET("transport-req.json")
    suspend fun getAllTransportReqMap(): Response<Map<String, TransportReq>>

    @GET("transport-req/{key}.json")
    suspend fun getTransportReqByKey(
        @Path("key") key: String
    ): Response<TransportReq>

    @PUT("transport-req/{key}.json")
    suspend fun updateTransportReqByKey(
        @Path("key") key: String,
        @Body transportReq: TransportReq
    ): Response<Unit>

    @DELETE("transport-req/{key}.json")
    suspend fun deleteTransportReqByKey(
        @Path("key") key: String
    ): Response<Unit>

    @POST("transport-vol.json")
    suspend fun addTransportVol(
        @Body transportVol: TransportVol
    ): Response<Unit>

    @GET("transport-vol.json")
    suspend fun getAllTransportVolMap(): Response<Map<String, TransportVol>>

    @PUT("transport-vol/{key}.json")
    suspend fun updateTransportVolByKey(
        @Path("key") key: String,
        @Body transportVol: TransportVol
    ): Response<Unit>

    @DELETE("transport-vol/{key}.json")
    suspend fun deleteTransportVolByKey(
        @Path("key") key: String
    ): Response<Unit>
}