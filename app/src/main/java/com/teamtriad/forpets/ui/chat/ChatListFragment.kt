package com.teamtriad.forpets.ui.chat

import ChatList
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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

class ChatListFragment : Fragment(), ChatListRecyclerViewAdapter.OnItemClickListener,
    ChatListRecyclerViewAdapter.OnItemLongClickListener {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatListDatabase: DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private val chatListAdapter = ChatListRecyclerViewAdapter(this, this)
    private var currentNickname: String = ""
    private var currentUserID: String? = ""

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
        chatListDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child("chat")
        userDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child("user")
        fetchCurrentUserInfo()
    }

    private fun fetchCurrentUserInfo() {
        currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        userDatabase.child(currentUserID!!)
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

                    if (friendChatData?.volUid == currentUserID || friendChatData?.reqUid == currentUserID) {
                        val isVolNicknameCurrentUser = friendChatData?.volUid == currentUserID

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
                                roomKey = chatSnapshot.key.toString(),
                                unreadMessageCount = 0
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

        resetUnreadMessageCount(roomKey)

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

    private fun resetUnreadMessageCount(roomKey: String) {
        val chatRef = chatListDatabase.child(roomKey)
        chatRef.child("unreadMessageCount").setValue(0)
    }

    override fun onItemLongClick(chatList: ChatList) {
        leaveChatRoom(chatList.roomKey)
    }

    private fun leaveChatRoom(roomKey: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setMessage("채팅방을 나가시겠습니까?")
            .setCancelable(false)
            .setPositiveButton("예") { dialog, id ->
                deleteChatRoomIfEmpty(roomKey)
            }
            .setNegativeButton("아니요") { dialog, id ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("채팅방 나가기")
        alert.show()
    }

    private fun deleteChatRoomIfEmpty(roomKey: String) {
        val chatRef = chatListDatabase.child(roomKey)

        chatRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updatedChatListData = chatListAdapter.currentList.toMutableList()
                val removedIndex = updatedChatListData.indexOfFirst { it.roomKey == roomKey }

                if (removedIndex != -1) {
                    updatedChatListData.removeAt(removedIndex)
                    chatListAdapter.submitList(updatedChatListData)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "채팅방을 나갔습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "채팅방을 나가는데 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
