package com.teamtriad.forpets.data

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.teamtriad.forpets.data.source.network.AdoptService
import com.teamtriad.forpets.data.source.network.model.AbandonmentInfo
import com.teamtriad.forpets.util.toYyyyMmDd
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.Date

class AdoptRepository(private val adoptService: AdoptService) {

    suspend fun getAbandonmentInfos(pageNo: Int): List<AbandonmentInfo>? {
        try {
            val response = adoptService.getAbandonmentPublic(
                "20240101",
                Date().toYyyyMmDd(),
                pageNo.toString()
            )

            return response.body()!!
                .response
                .body
                ?.items
                ?.item
        } catch (e: HttpException) {
            Log.e("AdoptRepository", "retrofit2.HttpException occurred.")

            return null
        } catch (e: SocketTimeoutException) {
            Log.e("AdoptRepository", "java.net.SocketTimeoutException occurred.")

            return null
        } catch (e: JsonDataException) {
            Log.e(
                "AdoptRepository",
                "One of the required fields may be missing from the response message."
            )

            return listOf()
        }
    }
}