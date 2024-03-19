package com.teamtriad.forpets.data

import android.util.Log
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import com.teamtriad.forpets.data.source.network.model.User

class UserRepository(private val databaseService: RemoteDatabaseService) {

    suspend fun addUserByUid(uid: String, user: User) {
        try {
            databaseService.addUserByUid(uid, user)
        } catch (e: Exception) {
            Log.e("UserRepository", e.message.toString())
        }
    }

    suspend fun getUserByUid(uid: String): User? {
        try {
            val response = databaseService.getUserByUid(uid)

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", e.message.toString())
        }

        return null
    }

    suspend fun getUsersMap(): Map<String, User>? {
        try {
            val response = databaseService.getUsersMap()

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", e.message.toString())
        }

        return null
    }

    suspend fun getUserNicknameByUid(uid: String): String? {
        try {
            val response = databaseService.getUserNicknameByUid(uid)

            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", e.message.toString())
        }

        return null
    }

    suspend fun updateUserByUid(uid: String, user: User) {
        try {
            databaseService.updateUserByUid(uid, user)
        } catch (e: Exception) {
            Log.e("UserRepository", e.message.toString())
        }
    }

    suspend fun deleteUserByUid(uid: String) {
        try {
            databaseService.deleteUserByUid(uid)
        } catch (e: Exception) {
            Log.e("UserRepository", e.message.toString())
        }
    }
}