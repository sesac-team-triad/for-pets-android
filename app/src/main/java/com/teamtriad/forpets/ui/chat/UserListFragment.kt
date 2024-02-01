package com.teamtriad.forpets.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.teamtriad.forpets.databinding.FragmentUserListBinding
import com.teamtriad.forpets.ui.chat.adapter.UserListRecyclerViewAdapter

class UserListFragment : Fragment(), UserListRecyclerViewAdapter.OnItemClickListener {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val userList = mutableListOf<Users>()
    private lateinit var userAdapter: UserListRecyclerViewAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child(
                "user"
            )

        userAdapter = UserListRecyclerViewAdapter(this)
        binding.rvUserList.adapter = userAdapter

        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(Users::class.java)
                if (user != null) {
                    userList.add(user)
                    userAdapter.submitList(userList.toList())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(user: Users) {
        val action =
            UserListFragmentDirections.actionUserListFragmentToChatroomFragment(
                roomId = user.email,
                friendName = user.nickname
            )
        findNavController().navigate(action)
    }
}

data class Users(
    val email: String = "",
    val nickname: String = ""
)