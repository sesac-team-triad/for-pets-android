package com.teamtriad.forpets.ui.chat

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
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
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
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
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var receiverUID = ""
    private var receiverName = ""
    private var senderName = ""
    private var appointmentKey = ""
    private var isAppointmentBooked = false
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

        initializeView()

        val reqUid = args.reqUid
        val reqNickname = args.reqNickname
        val volUid = args.volUid
        val volNickname = args.volNickname

        receiverUID = if (currentUserID == volUid) {
            reqUid
        } else {
            volUid
        }

        receiverName = if (currentUserID == volUid) {
            reqNickname
        } else {
            volNickname
        }

        senderName = if (currentUserID == volUid) {
            volNickname
        } else {
            reqNickname
        }


        adapter = ChatRoomRecyclerViewAdapter(currentUserID)
        binding.rvChatRoom.adapter = adapter

        binding.toolbar.subtitle = receiverName

        binding.toolbar.setNavigationOnClickListener {
            onBackButtonPress()
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPress()
        }

        binding.btnSend.setOnClickListener {
            sendMessage()
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etInputWindow.windowToken, 0)
        }

        binding.btnSchedule.setOnClickListener {
            showAppointmentBookingDialog()
        }

        loadMessages()
        updateProgress()
    }

    private fun initializeView() {
        requireActivity().window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun onBackButtonPress() {
        requireActivity().window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
        findNavController().navigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage(appointmentKey: String? = null) {

        val inputWindow = binding.etInputWindow
        val content = inputWindow.text.toString().trim()

        if (content.isNotEmpty()) {
            val roomKey = args.roomKey
            val reqUid = args.reqUid
            val reqNickname = args.reqNickname
            val volUid = args.volUid
            val volNickname = args.volNickname
            val transportReqKey = args.transportReqKey

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = timeFormat.format(Date())
            val messages = ChatMessage(content, time, currentUserID, senderName)

            database = FirebaseDatabase.getInstance(databaseUrl).reference
            val chatRef = database.child("chat").child(roomKey)

            chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val initialData = mapOf(
                            "reqUid" to reqUid,
                            "reqNickname" to reqNickname,
                            "volUid" to volUid,
                            "volNickname" to volNickname,
                            "transportReqKey" to transportReqKey,
                            "appointmentKey" to appointmentKey
                        )
                        chatRef.setValue(initialData)
                    }
                    chatRef.child("messages").push().setValue(messages)
                    adapter.addMessage(messages)
                    if (adapter.itemCount > 0) {
                        binding.rvChatRoom.smoothScrollToPosition(adapter.itemCount - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
//            sendNotification(receiverUID, receiverName, content)
            inputWindow.text.clear()
        }
    }

    private fun sendNotification(
        receiverUid: String,
        receiverName: String,
        messageContent: String
    ) {
        val receiverFCMToken = getUserFCMToken(receiverUid)
        val data = hashMapOf(
            "title" to "새로운 메시지",
            "body" to "$receiverName: $messageContent"
        )

        val message = RemoteMessage.Builder(receiverFCMToken)
            .setData(data)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }

    private fun getUserFCMToken(userUid: String): String {
        val chatListDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child("user")

        var userToken = ""
        chatListDatabase.child(userUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userToken = snapshot.child("token").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return userToken
    }

    private fun loadMessages() {
        val roomKey = args.roomKey
        database = FirebaseDatabase.getInstance(databaseUrl).reference
        val chatRef = database.child("chat").child(roomKey).child("messages")
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<ChatMessage>()

                for (messageSnapshot in snapshot.children) {
                    val content =
                        messageSnapshot.child("content").getValue(String::class.java) ?: ""
                    val time = messageSnapshot.child("time").getValue(String::class.java) ?: ""
                    val senderUid =
                        messageSnapshot.child("senderUid").getValue(String::class.java) ?: ""
                    val senderName =
                        messageSnapshot.child("senderName").getValue(String::class.java) ?: ""

                    val message = ChatMessage(content, time, senderUid, senderName)
                    messages.add(message)
                }

                adapter.submitList(messages)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun showAppointmentBookingDialog() {
        val positiveButtonText = if (isAppointmentBooked) "출발하기" else "약속잡기"
        val negativeButtonText = if (isAppointmentBooked) "약속취소" else "닫기"

        val appointmentBookingDialog = AlertDialog.Builder(requireContext())
            .setTitle("약속잡기")
            .setMessage("약속을 예약하시겠습니까?")
            .setPositiveButton(positiveButtonText) { _, _ ->
                if (isAppointmentBooked) {
                    updateProgress()
                } else {
                    showScheduleInputDialog()
                }
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                val roomKey = args.roomKey
                database = FirebaseDatabase.getInstance(databaseUrl).reference
                database.child("chat").child(roomKey)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val appointmentKey = snapshot.child("appointment-key").value.toString()
                            database.child("appointment").child(appointmentKey).setValue(null)
                            database.child("chat").child(roomKey).child("appointment-key")
                                .setValue(null)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
            .create()

        appointmentBookingDialog.show()
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
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onInputReceived(
                    name: String,
                    from: String,
                    to: String
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
                    val date =
                        SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(
                            selectedDate
                        )
                    val time =
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                            selectedDate
                        )

                    val totalDate =
                        SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(
                            selectedDate
                        )

                    val volUid = args.volUid
                    val reqUid = args.reqUid
                    val transportReqKey = args.transportReqKey
                    val progress = 0
                    val appointmentData = mapOf(
                        "name" to name,
                        "from" to from,
                        "to" to to,
                        "date" to date,
                        "time" to time,
                        "vol-uid" to volUid,
                        "req-uid" to reqUid,
                        "transport-req-key" to transportReqKey,
                        "progress" to progress,
                        "totalDate" to totalDate
                    )


                    val newAppointmentRef = appointmentDatabase.push()
                    appointmentKey = newAppointmentRef.key.toString()

                    newAppointmentRef.setValue(appointmentData)

                    val roomKey = args.roomKey
                    database.child("chat").child(roomKey)
                        .child("appointment-key")
                        .setValue(appointmentKey)

                    val message = "약속이 예약되었습니다.\n" +
                            "보호동물 이름: $name\n" +
                            "출발지역: $from\n" +
                            "도착지역: $to\n"
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

        isAppointmentBooked = true
    }

    private fun updateProgress() {
        val appointmentDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child("appointment")
        appointmentDatabase.child(appointmentKey).child("progress").setValue(1)
    }

    data class ChatMessage(
        var content: String,
        var time: String,
        var senderUid: String,
        var senderName: String
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

