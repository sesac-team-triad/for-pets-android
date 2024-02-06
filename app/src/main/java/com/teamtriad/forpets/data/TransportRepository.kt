package com.teamtriad.forpets.data

import android.util.Log
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.data.source.network.model.TransportVol

class TransportRepository(private val databaseService: RemoteDatabaseService) {

    suspend fun addTransportReq(transportReq: TransportReq) {
        try {
            databaseService.addTransportReq(transportReq)
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
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

    suspend fun updateTransportReqByKey(key: String, transportReq: TransportReq) {
        try {
            databaseService.updateTransportReqByKey(key, transportReq)
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }
    }

    suspend fun deleteTransportReqByKey(key: String) {
        try {
            databaseService.deleteTransportReqByKey(key)
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }
    }

    suspend fun addTransportVol(transportVol: TransportVol) {
        try {
            databaseService.addTransportVol(transportVol)
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }
    }

    suspend fun getAllTransportVolMap(): Map<String, TransportVol>? {
        try {
            val response = databaseService.getAllTransportVolMap()

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }

        return null
    }

    suspend fun updateTransportVolByKey(key: String, transportVol: TransportVol) {
        try {
            databaseService.updateTransportVolByKey(key, transportVol)
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }
    }

    suspend fun deleteTransportVolByKey(key: String) {
        try {
            databaseService.deleteTransportVolByKey(key)
        } catch (e: Exception) {
            Log.e("TransportRepository", e.message.toString())
        }
    }
}