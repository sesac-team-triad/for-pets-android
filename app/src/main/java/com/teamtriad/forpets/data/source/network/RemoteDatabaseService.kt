package com.teamtriad.forpets.data.source.network

import com.teamtriad.forpets.data.source.network.model.Appointment
import com.teamtriad.forpets.data.source.network.model.CompletedDate
import com.teamtriad.forpets.data.source.network.model.District
import com.teamtriad.forpets.data.source.network.model.KeyResponse
import com.teamtriad.forpets.data.source.network.model.Moving
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.data.source.network.model.TransportVol
import com.teamtriad.forpets.data.source.network.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @GET("location.json")
    suspend fun getAllCountyMap(): Response<Map<String, Map<String, District>>>

    @POST("appointment.json")
    suspend fun addAppointment(
        @Body appointment: Appointment
    ): Response<KeyResponse>

    @GET("appointment.json")
    suspend fun getAllAppointmentMap(): Response<Map<String, Appointment>>

    @GET("appointment/{key}.json")
    suspend fun getAppointmentByKey(
        @Path("key") key: String
    ): Response<Appointment>

    @PATCH("appointment/{key}.json")
    suspend fun updateAppointmentProgressByKey(
        @Path("key") key: String,
        @Body progress: Int
    ): Response<Unit>

    @PATCH("appointment/{key}.json")
    suspend fun updateAppointmentCompletedDateByKey(
        @Path("key") key: String,
        @Body completedDate: CompletedDate
    ): Response<Unit>

    @DELETE("appointment/{key}.json")
    suspend fun deleteAppointmentByKey(
        @Path("key") key: String
    ): Response<Unit>

    @POST("moving.json")
    suspend fun addMoving(
        @Body moving: Moving
    ): Response<Unit>

    @GET("moving.json")
    suspend fun getAllMovingMap(): Response<Map<String, Moving>>

    @DELETE("moving/{key}.json")
    suspend fun deleteMovingByKey(
        @Path("key") key: String
    ): Response<Unit>

    @POST("user/{uid}.json")
    suspend fun addUserByUid(
        @Path("uid") uid: String,
        @Body user: User
    ): Response<Unit>

    @GET("user.json")
    suspend fun getUsersMap(): Response<Map<String, User>>

    @GET("user/{uid}.json")
    suspend fun getUserByUid(
        @Path("uid") uid: String
    ): Response<User>

    @GET("user/{uid}/nickname.json")
    suspend fun getUserNicknameByUid(
        @Path("uid") uid: String
    ): Response<String>

    @PUT("user/{uid}.json")
    suspend fun updateUserByUid(
        @Path("uid") uid: String,
        @Body user: User
    ): Response<Unit>

    @DELETE("user/{uid}.json")
    suspend fun deleteUserByUid(
        @Path("uid") uid: String
    ): Response<Unit>
}