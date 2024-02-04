package com.teamtriad.forpets.ui.chat

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teamtriad.forpets.databinding.FragmentChatRoomBinding
import com.teamtriad.forpets.ui.chat.adapter.ChatRoomRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChatRoomFragment : Fragment() {
    private val args: ChatRoomFragmentArgs by navArgs()

    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private lateinit var adapter: ChatRoomRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reqNickname = args.reqNickname

        binding.toolbar.subtitle = reqNickname
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
        binding.btnSchedule.setOnClickListener {
            showScheduleInputDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage() {
        val roomId = args.roomId
        val reqUid = args.reqUid
        val reqNickname = args.reqNickname
        val volUid = args.volUid
        val volNickname = args.volNickname
        val transportReqKey = args.transportReqKey
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        val inputWindow = binding.etInputWindow
        val content = inputWindow.text.toString().trim()
        if (content.isNotEmpty()) {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = timeFormat.format(Date())
            val messages = ChatMessage(content, time, senderUid!!)

            database = FirebaseDatabase.getInstance(databaseUrl).reference
            val chatRef = database.child("chat").child(roomId)

            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val initialData = mapOf(
                            "reqUid" to reqUid,
                            "reqNickname" to reqNickname,
                            "volUid" to volUid,
                            "volNickname" to volNickname,
                            "transportReqKey" to transportReqKey
                        )
                        chatRef.setValue(initialData)
                    }
                    chatRef.child("messages").push().setValue(messages)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            inputWindow.text.clear()
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
                        FirebaseDatabase.getInstance(databaseUrl).reference.child(
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


        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->

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


    data class ChatMessage(
        var content: String,
        var time: String,
        var senderUid: String
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
