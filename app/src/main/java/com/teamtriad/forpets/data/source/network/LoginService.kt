package com.teamtriad.forpets.data.source.network

import retrofit2.Call
import retrofit2.http.GET

interface LoginService {
    @GET("User.json")
    fun getAllUserData(): Call<Map<String, User>>
}