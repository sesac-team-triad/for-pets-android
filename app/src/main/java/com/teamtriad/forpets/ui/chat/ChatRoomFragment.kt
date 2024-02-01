package com.teamtriad.forpets.ui.chat

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teamtriad.forpets.databinding.FragmentChatRoomBinding
import com.teamtriad.forpets.ui.chat.adapter.ChatRoomRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatRoomFragment : Fragment() {
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatRoomRecyclerViewAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private var myUid: String? = null
    private var receiverUid: String? = null
    private var senderEmail: String = ""
    private var senderName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatRoomRecyclerViewAdapter(messages, senderEmail)
        binding.rvChatRoom.adapter = adapter
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser ?: return
        receiverUid = arguments?.getString("receiverUid")
        getUserInfo()
        setupMessageListener()

        val roomId: String? = arguments?.getString("roomId") ?: createChatId(myUid!!, receiverUid!!)
        binding.btnSend.setOnClickListener {
            roomId?.let {
                val receiverName = arguments?.getString("friendName") ?: ""
                sendMessage(roomId, receiverName)
            }
        }

        binding.btnSchedule.setOnClickListener {
            showScheduleInputDialog(roomId)
        }
    }

    private fun setupMessageListener() {
        database = FirebaseDatabase.getInstance(databaseUrl).reference.child("chats").child(createChatId(myUid!!, receiverUid!!))
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessage::class.java)
                if (message != null) {
                    val receiverName = arguments?.getString("friendName")
                    message.senderEmail = senderEmail
                    message.senderName = senderName
                    message.receiverName = receiverName!!
                    messages.add(message)
                    adapter.notifyItemInserted(messages.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sendMessage(receiverUid: String, receiverName: String) {
        val content = binding.etInputWindow.text.toString().trim()
        if (content.isNotEmpty()) {
            user.let {
                val message = ChatMessage(
                    senderEmail,
                    receiverUid,
                    content,
                    System.currentTimeMillis(),
                    senderName,
                    receiverName,
                )
                database.push().setValue(message)
                binding.etInputWindow.text.clear()
            }
        }
    }

    private fun getUserInfo() {
        myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        user.let {
            database =
                FirebaseDatabase.getInstance(databaseUrl).getReference("user")
            database.child(myUid!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nickname = snapshot.child("nickname").value.toString()
                    val email = snapshot.child("email").value.toString()
                    senderEmail = email
                    senderName = nickname
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun showScheduleInputDialog(roomId: String?) {
        // 코드는 여기에 추가하세요.
    }

    private fun createChatId(senderUid: String, receiverUid: String): String {
        val uids = listOf(senderUid, receiverUid).sorted()
        return "${uids[0]}_${uids[1]}"
    }

    data class ChatMessage(
        var senderEmail: String = "",
        val receiverUid: String = "",
        val content: String = "",
        val timestamp: Long = 0,
        var senderName: String = "",
        var receiverName: String = ""
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
