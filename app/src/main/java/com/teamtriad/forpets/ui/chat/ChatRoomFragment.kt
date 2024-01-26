import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.data.source.network.ChatRoom
import com.teamtriad.forpets.data.source.network.ChatService
import com.teamtriad.forpets.databinding.FragmentChatRoomBinding
import com.teamtriad.forpets.ui.chat.adapter.ChatRoomRecyclerViewAdapter
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomFragment : Fragment() {
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val chatRoomAdapter = currentUserId?.let { ChatRoomRecyclerViewAdapter(it) }

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
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChatRoom.adapter = chatRoomAdapter
        fetchChatRoomData()

        binding.btnSend.setOnClickListener {
            val message = binding.etInputWindow.text.toString()

            if (message.isNotEmpty()) {
                sendChatMessageToServer(message)
                binding.etInputWindow.text.clear()
            }
        }
    }

    private fun fetchChatRoomData() {
        chatService.getChatRoom().enqueue(object : Callback<Map<String, ChatRoom>> {
            override fun onResponse(
                call: Call<Map<String, ChatRoom>>,
                response: Response<Map<String, ChatRoom>>
            ) {
                if (response.isSuccessful) {
                    val chatRoomMap = response.body()
                    chatRoomAdapter?.submitList(chatRoomMap?.values?.toList())
                } else {
                }
            }

            override fun onFailure(call: Call<Map<String, ChatRoom>>, t: Throwable) {
            }
        })
    }

    private fun sendChatMessageToServer(message: String) {
        val newChatMessage = currentUserId?.let {
            ChatRoom(
                messageId = UUID.randomUUID().toString(),
                senderName = it,
                messageContent = message,
                sentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                isMyMessage = true
            )
        }

        try {
            val response = chatService.sendChatMessage(newChatMessage!!).execute()
            if (response.isSuccessful) {
                // 성공 처리
            } else {
                // 실패 처리
            }
        } catch (e: IOException) {
            // 실패 처리
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
