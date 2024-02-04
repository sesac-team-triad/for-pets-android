package com.teamtriad.forpets.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        chatService.getChatList().enqueue(object : Callback<Map<String, ChatList>> {
            override fun onResponse(
                call: Call<Map<String, ChatList>>,
                response: Response<Map<String, ChatList>>
            ) {
                if (response.isSuccessful) {
                    val chatListMap = response.body()
                    chatListAdapter.submitList(chatListMap?.values?.toList())
                } else {
                    Toast.makeText(requireContext(), "채팅목록 가져오기 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, ChatList>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "채팅 목록을 불러오는데 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onItemClick(chatList: ChatList) {
        val action =
            ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(
                roomKey = "roomId",
                reqUid = "reqUid",
                reqNickname = "reqNickname",
                volUid = "volUid",
                volNickname = "volNickname",
                transportReqKey = "transportReqKey"
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
