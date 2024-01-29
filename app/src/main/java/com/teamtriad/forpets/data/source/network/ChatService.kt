package com.teamtriad.forpets.data.source.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {

    @GET("conversation/chatlist.json")
    fun getChatList(): Call<Map<String, ChatList>>

    @GET("conversation/chatroom/{roomId}.json")
    fun getChatRoom(@Path("roomId") roomId: String): Call<ChatRoom>

    @POST("conversation/chatroom.json")
    fun sendChatMessage(@Body chatRoom: ChatRoom): Call<ResponseBody>

}
