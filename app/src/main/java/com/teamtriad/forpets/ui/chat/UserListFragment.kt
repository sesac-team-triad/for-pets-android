package com.teamtriad.forpets.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teamtriad.forpets.data.source.network.User
import com.teamtriad.forpets.databinding.FragmentUserListBinding
import com.teamtriad.forpets.ui.chat.adapter.UserListRecyclerViewAdapter

class UserListFragment : Fragment(), UserListRecyclerViewAdapter.OnItemClickListener {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDatabase: DatabaseReference
    private lateinit var chatDatabase: DatabaseReference
    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private lateinit var userAdapter: UserListRecyclerViewAdapter
    private var currentNickname: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child(
                "user"
            )
        chatDatabase =
            FirebaseDatabase.getInstance(databaseUrl).reference.child(
                "chat"
            )
        userAdapter = UserListRecyclerViewAdapter(this)
        binding.rvUserList.adapter = userAdapter

        fetchAllUsersInfo()
        fetchCurrentUserInfo()
    }

    private fun fetchAllUsersInfo() {
        val userList = mutableListOf<User>()
        userDatabase.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val reqUid = snapshot.key.toString()
                val nickname = snapshot.child("nickname").value.toString()
                val user = User(reqUid, "", "", nickname, "")
                userList.add(user)
                userAdapter.submitList(userList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchCurrentUserInfo() {
        val volUid = FirebaseAuth.getInstance().currentUser?.uid
        userDatabase.child(volUid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentNickname = snapshot.child("nickname").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun onItemClick(user: User) {

        val reqUid = user.uid
        val reqNickname = user.nickname
        val volUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val volNickname = currentNickname

        findExistingRoomKey(reqUid, volUid) { existingRoomKey ->
            val roomKey = existingRoomKey ?: chatDatabase.push().key.toString()
            val transportReqKey = chatDatabase.push().child("transportReqKey").push().key ?: ""

            val action = UserListFragmentDirections.actionUserListFragmentToChatroomFragment(
                roomKey = roomKey,
                reqUid = reqUid,
                reqNickname = reqNickname,
                volUid = volUid,
                volNickname = volNickname,
                transportReqKey = transportReqKey
            )
            findNavController().navigate(action)
        }
    }

    private fun findExistingRoomKey(reqUid: String, volUid: String, callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance(databaseUrl).reference.child("chat")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var roomKey: String? = null

                for (chatSnapshot in snapshot.children) {
                    val reqUidInChat = chatSnapshot.child("reqUid").getValue(String::class.java)
                    val volUidInChat = chatSnapshot.child("volUid").getValue(String::class.java)

                    if ((reqUidInChat == reqUid && volUidInChat == volUid) ||
                        (reqUidInChat == volUid && volUidInChat == reqUid)
                    ) {
                        roomKey = chatSnapshot.key
                        break
                    }
                }
                callback(roomKey)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}