package com.teamtriad.forpets.data.source.network

import retrofit2.Call
import retrofit2.http.GET

interface LoginService {
    @GET("Users.json")
    fun getAllUserData(): Call<Map<String, User>>
}