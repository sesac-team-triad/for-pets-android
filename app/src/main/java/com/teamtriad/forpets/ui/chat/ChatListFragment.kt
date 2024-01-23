package com.teamtriad.forpets.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teamtriad.forpets.data.source.network.User
import com.teamtriad.forpets.ui.chat.adapter.ChatListRecyclerViewAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.LoginService
import com.teamtriad.forpets.databinding.FragmentChatListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ChatListFragment : Fragment(), ChatListRecyclerViewAdapter.OnItemClickListener {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private val userListAdapter = ChatListRecyclerViewAdapter(this)

    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private val loginService: LoginService by lazy {
        createApiService()
    }

    private fun createApiService(): LoginService {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(databaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(LoginService::class.java)
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

        binding.rvUserList.adapter = userListAdapter
        fetchUserData()
    }

    private fun fetchUserData() {
        val call = loginService.getAllUserData()

        call.enqueue(object : Callback<Map<String, User>> {
            override fun onResponse(
                call: Call<Map<String, User>>,
                response: Response<Map<String, User>>
            ) {
                if (response.isSuccessful) {
                    try {
                        val allUserData = response.body()
                        val userList = mutableListOf<User>()

                        allUserData?.values?.forEach { user ->
                            val nickname = user.nickname
                            val newUser = User("", "", nickname)
                            userList.add(newUser)
                        }

                        userListAdapter.submitList(userList)
                    } catch (e: Exception) {
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<Map<String, User>>, t: Throwable) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(user: User) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(nickname = user.nickname)
        findNavController().navigate(action)
    }
}
