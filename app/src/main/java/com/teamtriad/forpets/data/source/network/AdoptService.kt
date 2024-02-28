package com.teamtriad.forpets.data.source.network

import com.teamtriad.forpets.BuildConfig.ABANDONMENT_API_KEY
import com.teamtriad.forpets.data.source.network.model.AbandonmentPublic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AdoptService {

    @GET(
        "1543061/abandonmentPublicSrvc/abandonmentPublic?serviceKey=${ABANDONMENT_API_KEY}" +
                "&state=protect&_type=json"
    )
    suspend fun getAbandonmentPublic(
        @Query("bgnde") bgnde: String,
        @Query("endde") endde: String,
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String = "30",
    ): Response<AbandonmentPublic>
}