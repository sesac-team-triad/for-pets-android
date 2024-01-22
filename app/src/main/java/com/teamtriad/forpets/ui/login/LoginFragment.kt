package com.teamtriad.forpets.ui.login

import ApiService
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtriad.forpets.R
import com.teamtriad.forpets.UserData
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


    // apiService를 처음 사용할 때 createApiService() 함수가 호출 후 캐시된 값이 계속해서 반환
    private val apiService: ApiService by lazy {
        createApiService()
    }

    private fun createApiService(): ApiService {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(databaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(ApiService::class.java)
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
        loginButton()
        signUpButton()
    }

    private fun loginButton() {
        binding.btnLogin.setOnClickListener {
            checkIfUserExists()
        }
    }

    private fun signUpButton() {
        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpUserFragment)
        }
    }

    private fun checkIfUserExists() {
        val call = apiService.getAllUserData()

        call.enqueue(object : Callback<Map<String, UserData>> {
            override fun onResponse(
                call: Call<Map<String, UserData>>,
                response: Response<Map<String, UserData>>
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
                            showToast("환영합니다! ${foundUser.nickname}님")
                            findNavController().navigate(R.id.action_loginFragment_to_signUpUserFragment)
                        } else {
                            showToast("사용자가 존재하지 않거나 비밀번호가 일치하지 않습니다!")
                        }
                    } catch (e: Exception) {
                        Log.e("LoginFragment", "$e")
                    }
                } else {
                    Log.e("LoginFragment", "${response.code()}")
                    showToast("사용자 확인 실패! (서버 오류)")
                }
            }

            override fun onFailure(call: Call<Map<String, UserData>>, t: Throwable) {
                showToast("사용자 확인 실패! (네트워크 오류)")
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
