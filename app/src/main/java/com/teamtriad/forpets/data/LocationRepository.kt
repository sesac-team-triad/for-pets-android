package com.teamtriad.forpets.data

import android.util.Log
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import com.teamtriad.forpets.data.source.network.model.District

class LocationRepository(private val databaseService: RemoteDatabaseService) {

    suspend fun getAllCountyMap(): Map<String, Map<String, District>>? {
        try {
            val response = databaseService.getAllCountyMap()

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", e.message.toString())
        }

        return null
    }
}