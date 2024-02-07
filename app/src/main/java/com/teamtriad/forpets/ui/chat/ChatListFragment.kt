package com.teamtriad.forpets.ui.chat

import ChatList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.teamtriad.forpets.databinding.FragmentChatListBinding
import com.teamtriad.forpets.ui.chat.adapter.ChatListRecyclerViewAdapter

class ChatListFragment : Fragment(), ChatListRecyclerViewAdapter.OnItemClickListener {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatListDatabase: DatabaseReference
    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private lateinit var userDatabase: DatabaseReference
    private val chatListAdapter = ChatListRecyclerViewAdapter(this)
    private var currentNickname: String = ""
    private var volUid: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatListDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child("chat")
        binding.rvChatList.adapter = chatListAdapter
        userDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child(
                "user"
            )
        fetchCurrentUserInfo()
    }

    private fun fetchCurrentUserInfo() {
        volUid = FirebaseAuth.getInstance().currentUser?.uid
        userDatabase.child(volUid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentNickname = snapshot.child("nickname").value.toString()
                    fetchChatListData()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun fetchChatListData() {
        chatListDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatListData = mutableListOf<ChatList>()

                for (chatSnapshot in snapshot.children) {
                    val friendChatData = chatSnapshot.getValue(ChatList::class.java)
                    val lastMessage = chatSnapshot.child("messages").children.lastOrNull()
                    val lastMessageContent =
                        lastMessage?.child("content")?.getValue(String::class.java) ?: ""
                    val lastMessageTime =
                        lastMessage?.child("time")?.getValue(String::class.java) ?: ""

                    if (friendChatData?.volUid == volUid || friendChatData?.reqUid == volUid) {
                        val isVolNicknameCurrentUser =
                            lastMessage?.child("volNickname")?.value.toString() == currentNickname
                        val friendName = if (isVolNicknameCurrentUser) {
                            friendChatData?.reqNickname ?: ""
                        } else {
                            friendChatData?.volNickname ?: ""
                        }
                        chatListData.add(
                            friendChatData!!.copy(
                                lastMessage = lastMessageContent,
                                lastMessageTime = lastMessageTime,
                                friendName = friendName,
                                roomKey = chatSnapshot.key.toString()
                            )
                        )
                    }

                }
                chatListData.sortByDescending { it.lastMessageTime }
                chatListAdapter.submitList(chatListData)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "채팅 목록을 불러오는데 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun onItemClick(chatList: ChatList) {
        val roomKey = chatList.roomKey
        val reqUid = chatList.reqUid
        val reqNickname = chatList.friendName
        val volUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val volNickname = currentNickname
        val transportReqKey = chatList.transportReqKey
        val action =
            ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(
                roomKey = roomKey,
                reqUid = reqUid,
                reqNickname = reqNickname,
                volUid = volUid,
                volNickname = volNickname,
                transportReqKey = transportReqKey
            )
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
