package com.teamtriad.forpets.data

import android.util.Log
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import com.teamtriad.forpets.data.source.network.model.TransportReq

class TransportRepository(private val databaseService: RemoteDatabaseService) {

    suspend fun addTransportReq(transportReq: TransportReq): Boolean {
        return try {
            databaseService.addTransportReq(transportReq)
                .isSuccessful
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())

            false
        }
    }

    suspend fun getAllTransportReqMap(): Map<String, TransportReq>? {
        try {
            val response = databaseService.getAllTransportReqMap()

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }

        return null
    }

    suspend fun getTransportReqByKey(key: String): TransportReq? {
        try {
            val response = databaseService.getTransportReqByKey(key)

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }

        return null
    }

    /*    fun updateTransportReqByKey(key: String, transportReq: TransportReq) {
        }

        fun deleteTransportReqByKey(key: String) {
        }

        fun addTransportVol(transportVol: TransportVol) {
        }

        fun getAllTransportVolMap(): Map<String, TransportVol> {
        }

        fun getTransportVolByKey(key: String): TransportVol {
        }

        fun updateTransportVolByKey(key: String, transportVol: TransportVol) {
        }

        fun deleteTransportVolByKey(key: String) {
        }
        */
}