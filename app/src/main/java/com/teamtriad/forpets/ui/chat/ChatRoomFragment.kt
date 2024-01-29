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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.teamtriad.forpets.databinding.FragmentChatRoomBinding
import com.teamtriad.forpets.ui.chat.adapter.ChatRoomRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatRoomFragment : Fragment() {
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatRoomRecyclerViewAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "messages"
            )
        adapter = ChatRoomRecyclerViewAdapter()
        binding.rvChatRoom.adapter = adapter
        val roomId = arguments?.getString("roomId")
        binding.btnSend.setOnClickListener {
            if (roomId != null) {
                sendMessage(roomId)
            }
        }
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessage::class.java)
                if (message != null) {
                    messages.add(message)
                    adapter.notifyItemInserted(messages.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        binding.btnSchedule.setOnClickListener {
            showScheduleInputDialog()
        }
    }

    private fun showScheduleInputDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val hour = currentDate.get(Calendar.HOUR_OF_DAY)
        val minute = currentDate.get(Calendar.MINUTE)

        var selectedYear = year
        var selectedMonth = month
        var selectedDay = day
        var selectedHour = hour
        var selectedMinute = minute

        val scheduleInputDialog = ScheduleInputDialog(
            requireContext(),
            object : ScheduleInputDialog.OnScheduleInputListener {
                override fun onInputReceived(
                    dogName: String,
                    departureLocation: String,
                    destinationLocation: String
                ) {
                    val appointmentDatabase =
                        FirebaseDatabase.getInstance("https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                            "appointment"
                        )

                    val selectedDate = currentDate.apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, selectedDay)
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, 0)
                    }.time

                    // 약속 예약 데이터를 생성
                    val formattedDate =
                        SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(
                            selectedDate
                        )
                    val appointmentData = mapOf(
                        "dogName" to dogName,
                        "departureLocation" to departureLocation,
                        "destinationLocation" to destinationLocation,
                        "appointmentTime" to formattedDate
                    )

                    appointmentDatabase.push().setValue(appointmentData)

                    val message = "약속이 예약되었습니다.\n" +
                            "강아지 이름: $dogName\n" +
                            "출발지역: $departureLocation\n" +
                            "도착지역: $destinationLocation\n" +
                            "약속 시간: $formattedDate"
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                }
            })

        // DatePickerDialog 표시
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            // TimePickerDialog 표시
            val timePickerDialog = TimePickerDialog(requireContext(), { _, hour, minute ->
                selectedYear = year
                selectedMonth = month
                selectedDay = day
                selectedHour = hour
                selectedMinute = minute
                scheduleInputDialog.show()
            }, hour, minute, true)

            timePickerDialog.show()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun sendMessage(receiverId: String) {
        val content = binding.etInputWindow.text.toString().trim()
        if (content.isNotEmpty()) {
            val user = auth.currentUser
            if (user != null) {
                val message = ChatMessage(
                    user.uid,
                    user.displayName ?: "",
                    receiverId,
                    content,
                    System.currentTimeMillis()
                )
                database.push().setValue(message)
                binding.etInputWindow.text.clear()
            }
        }
    }

    data class ChatMessage(
        val senderId: String = "",
        val senderName: String = "",
        val receiverId: String = "",
        val content: String = "",
        val timestamp: Long = 0
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
