package com.teamtriad.forpets.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.LoginService
import com.teamtriad.forpets.data.source.network.User
import com.teamtriad.forpets.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val databaseUrl =
        "https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private lateinit var userDB: DatabaseReference

    private var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null

    // apiService를 처음 사용할 때 createApiService() 함수가 호출 후 캐시된 값이 계속해서 반환
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        userDB = FirebaseDatabase.getInstance(databaseUrl).getReference("user")

        binding.btnLogin.setOnClickListener {
            checkIfUserExist()
            //checkIfUserExists()
        }
        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpUserFragment)
        }
    }

    private fun checkIfUserExist() {
        val email = binding.tietEmail.text.toString()
        val password = binding.tietPassword.text.toString()
        auth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            user = FirebaseAuth.getInstance().currentUser
            val userId = user!!.uid
            if (it.isSuccessful) {
                userDB.child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        showToast(getString(R.string.login_toast_welcome))
                        findNavController().navigate(R.id.action_loginFragment_to_transportFragment)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }
        }
    }


    private fun checkIfUserExists() {
        val call = loginService.getAllUserData()
        call.enqueue(object : Callback<Map<String, User>> {
            override fun onResponse(
                call: Call<Map<String, User>>,
                response: Response<Map<String, User>>
            ) {
                if (response.isSuccessful) {
                    try {
                        val allUserData = response.body()
                        val enteredEmail = binding.tietEmail.text.toString()
                        val enteredPassword = binding.tietPassword.text.toString()

                        val foundUser = allUserData?.values?.find {
                            it.email == enteredEmail && it.password == enteredPassword
                        }
                        if (foundUser != null) {
                            showToast(getString(R.string.login_toast_welcome))
                            val user = FirebaseAuth.getInstance().currentUser
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            Log.d("확인1", "$user")
                            Log.d("확인2", "$uid")
                            findNavController().navigate(R.id.action_loginFragment_to_transportFragment)
                        } else {
                            showToast(getString(R.string.login_toast_not_found))
                        }
                    } catch (e: Exception) {
                        Log.e("LoginFragment", "$e")
                    }
                } else {
                    Log.e("LoginFragment", "${response.code()}")
                    showToast(getString(R.string.login_toast_server_error))
                }
            }

            override fun onFailure(call: Call<Map<String, User>>, t: Throwable) {
                showToast(getString(R.string.login_toast_network_error))
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

