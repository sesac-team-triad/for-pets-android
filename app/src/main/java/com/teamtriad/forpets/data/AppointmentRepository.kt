package com.teamtriad.forpets.data

import android.util.Log
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import com.teamtriad.forpets.data.source.network.model.Appointment
import com.teamtriad.forpets.data.source.network.model.CompletedDate
import com.teamtriad.forpets.data.source.network.model.Moving

class AppointmentRepository(private val databaseService: RemoteDatabaseService) {

    suspend fun addAppointment(appointment: Appointment): String? {
        try {
            val response = databaseService.addAppointment(appointment)

            if (response.isSuccessful) {
                return response.body()
                    ?.key
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }

        return null
    }

    suspend fun getAllAppointmentMap(): Map<String, Appointment>? {
        try {
            val response = databaseService.getAllAppointmentMap()

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }

        return null
    }

    suspend fun getAppointmentByKey(key: String): Appointment? {
        try {
            val response = databaseService.getAppointmentByKey(key)

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }

        return null
    }

    suspend fun updateAppointmentProgressByKey(key: String, progress: Int) {
        try {
            databaseService.updateAppointmentProgressByKey(key, progress)
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }
    }

    suspend fun updateAppointmentCompletedDateByKey(key: String, completedDate: CompletedDate) {
        try {
            databaseService.updateAppointmentCompletedDateByKey(key, completedDate)
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }
    }

    suspend fun deleteAppointmentByKey(key: String) {
        try {
            databaseService.deleteAppointmentByKey(key)
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }
    }

    suspend fun addMoving(moving: Moving) {
        try {
            databaseService.addMoving(moving)
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }
    }

    suspend fun getAllMovingMap(): Map<String, Moving>? {
        try {
            val response = databaseService.getAllMovingMap()

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }

        return null
    }

    suspend fun deleteMovingByKey(key: String) {
        try {
            databaseService.deleteMovingByKey(key)
        } catch (e: Exception) {
            Log.e("AppointmentRepository", e.message.toString())
        }
    }
}