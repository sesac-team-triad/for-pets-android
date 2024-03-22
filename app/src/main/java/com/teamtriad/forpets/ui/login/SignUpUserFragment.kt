package com.teamtriad.forpets.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.User
import com.teamtriad.forpets.databinding.FragmentSignUpUserBinding

class SignUpUserFragment : Fragment() {

    private var _binding: FragmentSignUpUserBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth? = null

    private var checks = booleanArrayOf(false, false, false, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentSignUpUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkValidation()
        binding.btnSubmit.setOnClickListener {
            createUserData()
        }
    }

    private fun setupWatcher(editText: EditText, index: Int, validation: (String) -> Boolean) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checks[index] = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checks[index] = validation(s.toString())
                if (checks.all { it }) {
                    binding.btnSubmit.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    checks[index] = false
                }
            }
        })
    }

    private fun checkValidation() = with(binding) {
        btnSubmit.isEnabled = false

        setupWatcher(tietEmail, 0) {
            it.contains("@") && it.contains(".com")
        }
        setupWatcher(tietPassword, 1) { it.length >= 8 }
        setupWatcher(
            tietConfirmedPassword,
            2
        ) { it == tietPassword.text.toString() && it.length < 9 }
        setupWatcher(tietNickname, 3) { it.isNotEmpty() && it.length >= 2 }
    }

    private fun createUserData() = with(binding) {
        val email = tietEmail.text.toString()
        val password = tietPassword.text.toString()
        val nickname = tietNickname.text.toString()

        auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = User(email, password, nickname)
                saveUserDataToDatabase(user)
            } else {
                handleRegistrationFailure(task)
            }
        }
    }

    private fun saveUserDataToDatabase(user: User) {
        val databaseReference =
            FirebaseDatabase.getInstance("https://for-pets-77777-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users")
        val userId = databaseReference.push().key
        databaseReference.child(userId!!).setValue(user)
        showToast(getString(R.string.login_toast_welcome))
//        findNavController().navigate(R.id.action_signUpUserFragment_to_transportFragment)
    }

    private fun handleRegistrationFailure(task: Task<AuthResult>) {
        when (task.exception?.message) {
            "The email address is badly formatted." -> {
                showToast(getString(R.string.sign_up_toast_guide_email))
                binding.tietEmail.text = null
            }

            "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                showToast(getString(R.string.sign_up_toast_guide_password))
                binding.tietConfirmedPassword.text = null
                binding.tietPassword.text = null
            }

            "The email address is already in use by another account." -> {
                showToast(getString(R.string.sign_up_toast_exist_email))
                binding.tietEmail.text = null
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
