package com.teamtriad.forpets.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.data.source.network.ChatList
import com.teamtriad.forpets.data.source.network.ChatService
import com.teamtriad.forpets.databinding.FragmentChatListBinding
import com.teamtriad.forpets.ui.chat.adapter.ChatListRecyclerViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ChatListFragment : Fragment(), ChatListRecyclerViewAdapter.OnItemClickListener {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private val chatListAdapter = ChatListRecyclerViewAdapter(this)

    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private val chatService: ChatService by lazy {
        createApiService()
    }

    private fun createApiService(): ChatService {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(databaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(ChatService::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChatList.adapter = chatListAdapter
        fetchChatListData()
    }

    private fun fetchChatListData() {
        val call = chatService.getChatList()

        call.enqueue(object : Callback<Map<String, ChatList>> {
            override fun onResponse(
                call: Call<Map<String, ChatList>>,
                response: Response<Map<String, ChatList>>
            ) {
                if (response.isSuccessful) {
                    try {
                        val chatListData = response.body()
                        val chatList = mutableListOf<ChatList>()
                        Log.d("확인1", "$chatListData")
                        Log.d("확인2", "$chatList")
                        chatListData?.values?.forEach { chatListItem ->
                            chatList.add(chatListItem)
                        }

                        chatListAdapter.submitList(chatList)
                    } catch (e: Exception) {
                    }
                } else {
                    Log.d("확인1", "asd")
                    Log.d("확인2", "asd")
                }
            }

            override fun onFailure(call: Call<Map<String, ChatList>>, t: Throwable) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(chatList: ChatList) {
        val action =
            ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(
                friendName = chatList.friendName,
                roomId = chatList.roomId
            )
        findNavController().navigate(action)
    }
}
