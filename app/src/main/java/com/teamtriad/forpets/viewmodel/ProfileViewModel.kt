package com.teamtriad.forpets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtriad.forpets.ForPetsApplication.Companion.remoteDatabaseService
import com.teamtriad.forpets.data.UserRepository
import com.teamtriad.forpets.data.source.network.model.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val userRepository = UserRepository(remoteDatabaseService)

    private var _usersMap: Map<String, User>? = null
    val usersMap get() = _usersMap

    fun addUserByUid(uid: String, user: User) {
        viewModelScope.launch {
            userRepository.addUserByUid(uid, user)
        }
    }

    suspend fun getUserByUid(uid: String): User? {
        return userRepository.getUserByUid(uid)
    }

    fun getUsersMap() = viewModelScope.launch {
            _usersMap = userRepository.getUsersMap()
        }


    suspend fun getUserNicknameByUid(uid: String): String? {
        return userRepository.getUserNicknameByUid(uid)
    }

    fun updateUserByUid(uid: String, user: User) {
        viewModelScope.launch {
            userRepository.updateUserByUid(uid, user)
        }
    }

    fun deleteUserByUid(uid: String) {
        viewModelScope.launch {
            userRepository.deleteUserByUid(uid)
        }
    }
}