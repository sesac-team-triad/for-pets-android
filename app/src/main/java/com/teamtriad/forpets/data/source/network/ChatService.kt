package com.teamtriad.forpets.data.source.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatService {

    @GET("Conversation/ChatList.json")
    fun getChatList(): Call<Map<String, ChatList>>

    @GET("Conversation/ChatRoom.json")
    fun getChatRoom(): Call<Map<String, ChatRoom>>

    @POST("Conversation/ChatRoom.json")
    fun sendChatMessage(@Body chatRoom: ChatRoom): Call<ResponseBody>
}