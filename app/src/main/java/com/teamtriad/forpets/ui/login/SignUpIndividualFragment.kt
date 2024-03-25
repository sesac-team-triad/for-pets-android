package com.teamtriad.forpets.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.teamtriad.forpets.BuildConfig.EMAIL_ADDRESS
import com.teamtriad.forpets.BuildConfig.EMAIL_PASSOWRD
import com.teamtriad.forpets.BuildConfig.REMOTE_DATABASE_BASE_URL
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.User
import com.teamtriad.forpets.databinding.FragmentSignUpIndividualBinding
import com.teamtriad.forpets.util.setSafeOnClickListener
import com.teamtriad.forpets.viewmodel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SignUpIndividualFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()

    private lateinit var database: DatabaseReference

    private var _binding: FragmentSignUpIndividualBinding? = null
    private val binding get() = _binding!!

    private var isValidEmail = false
    private lateinit var authCode: String
    private lateinit var nickname: String
    private lateinit var password: String
    private var userEmail = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        database = Firebase.database(REMOTE_DATABASE_BASE_URL).getReference("user")
        _binding = FragmentSignUpIndividualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEmailAddress()
        checkAuthCode()
        checkUserNickname()
        checkPassword()
        setOnClickListener()
    }

    private fun setOnClickListener() = with(binding) {
        val softKeyboard =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        btnSendAuthCode.setSafeOnClickListener {
            if (isValidEmail && userEmail != tietEmail.text.toString()) {
                val tmpUserEmail = binding.tietEmail.text.toString()
                val userEmailList = mutableListOf<String?>()

                database.get().addOnSuccessListener { snapshot ->
                    snapshot.children.forEach {
                        userEmailList.add(
                            it.child("email").value.toString().trim()
                        )
                    }

                    if (tmpUserEmail in userEmailList) {
                        Snackbar.make(
                            requireView(),
                            R.string.sign_up_toast_exist_email,
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        authCode = sendEmail(tietEmail.text.toString())
                        softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)

                        Snackbar.make(
                            requireView(),
                            R.string.sign_up_email_transport_complete,
                            Snackbar.LENGTH_SHORT
                        ).show()

                        tietInputAuthCode.text?.clear()
                        tietPassword.text?.clear()
                        tietCheckPassword.text?.clear()
                        userEmail = ""
                        password = ""

                        btnSignup.apply {
                            isEnabled = false
                            isClickable = false
                        }
                        btnAuthenticate.visibility = View.VISIBLE
                        btnCompletedAuth.visibility = View.GONE
                    }
                }
            }
        }

        btnAuthenticate.setSafeOnClickListener {
            if (authCode == tietInputAuthCode.text.toString()) {
                btnAuthenticate.apply {
                    softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)
                    visibility = View.GONE
                    tietInputAuthCode.clearFocus()
                }

                btnCompletedAuth.visibility = View.VISIBLE
                userEmail = tietEmail.text.toString()
            } else {
                softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)

                Snackbar.make(
                    requireView(),
                    R.string.sign_up_auth_code_not_valid,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        btnVerifyNickname.setSafeOnClickListener {
            lifecycleScope.launch {
                viewModel.getUsersMap().join()
                val userNicknameList = mutableListOf<String>()
                viewModel.usersMap?.values?.forEach { userNicknameList.add(it.nickname) }

                if (userNicknameList.contains(binding.tietNickname.text.toString())) {
                    with(binding) {
                        btnVerifyNickname.visibility = View.VISIBLE
                        btnVerifiedNickname.visibility = View.GONE
                        tilNickname.error = getString(R.string.sign_up_nickname_error_message)
                    }
                } else {
                    with(binding) {
                        btnVerifyNickname.visibility = View.GONE
                        btnVerifiedNickname.visibility = View.VISIBLE
                        tilNickname.error = null
                        nickname = tietNickname.text.toString()
                    }
                }
            }
        }

        btnSignup.setSafeOnClickListener {
            val auth = Firebase.auth
            auth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("signup", "createUserWithEmail : Success")
                        val user = auth.currentUser

                        database.child(user?.uid!!)
                            .setValue(User(userEmail, password, nickname))

                        softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)
                        findNavController().navigate(R.id.action_signUpIndividualFragment_to_login2Fragment)

                        Snackbar.make(
                            requireView(),
                            R.string.sign_up_snackbar_welcome,
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        Log.w("signup", "createUseWithEmail : failure", task.exception)
                        Snackbar.make(
                            requireView(),
                            R.string.sign_up_snackbar_fail,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun sendEmail(userEmail: String): String {
        val email = EMAIL_ADDRESS
        val password = EMAIL_PASSOWRD
        val code = (10000..99999).random().toString()

        CoroutineScope(Dispatchers.IO).launch {
            val props = Properties()
            props.setProperty("mail.transport.protocol", "smtp")
            props.setProperty("mail.host", "smtp.gmail.com")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.socketFactory.fallback", "false")
            props.put("mail.smtp.ssl.enable", "true")
            props.setProperty("mail.smtp.quitwait", "false")

            val session = Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(email, password)
                    }
                })

            val message = MimeMessage(session)
            message.sender = InternetAddress(email)
            message.addRecipient(Message.RecipientType.TO, InternetAddress(userEmail))
            message.subject = getString(R.string.sign_up_email_title)
            message.setText(getString(R.string.sign_up_email_message, code))

            Transport.send(message)
        }
        return code
    }

    private fun checkEmailAddress() = with(binding) {
        tietEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()

                if (isValidEmail) {
                    tilEmail.error = null
                    btnSendAuthCode.apply {
                        isEnabled = true
                        isClickable = true
                    }
                } else {
                    tilEmail.error = getString(R.string.sign_up_email_error_message)
                    btnSendAuthCode.apply {
                        isEnabled = false
                        isClickable = false
                    }
                }
            }
        })
    }

    private fun checkAuthCode() = with(binding) {
        tietInputAuthCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 5) {
                    btnAuthenticate.apply {
                        isEnabled = true
                        isClickable = true
                    }
                } else {
                    btnAuthenticate.apply {
                        isEnabled = false
                        isClickable = false
                    }
                }
            }
        })
    }

    private fun checkUserNickname() = with(binding) {
        tietNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val pattern = Regex("[가-힣a-zA-Z0-9]{2,}")
                if (pattern.matches(s.toString())) {
                    btnVerifyNickname.apply {
                        isEnabled = true
                        isClickable = true
                        btnVerifyNickname.visibility = View.VISIBLE
                        btnVerifiedNickname.visibility = View.GONE
                        tilNickname.error = null
                    }
                } else {
                    btnVerifyNickname.apply {
                        isEnabled = false
                        isClickable = false
                        btnVerifyNickname.visibility = View.VISIBLE
                        btnVerifiedNickname.visibility = View.GONE
                        tilNickname.error = null
                    }
                }
            }
        })
    }

    private fun checkPassword() = with(binding) {
        var tmpPassword = ""

        tietPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val pattern =
                    Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=])[a-zA-Z\\d!@#\$%^&*()_+\\-=]{8,}\$")
                if (s?.length!! >= 8) {
                    if (pattern.matches(s.toString())) {
                        tietPassword.error = null
                        tmpPassword = s.toString()
                    } else {
                        tietPassword.error = getString(R.string.sign_up_til_password_helper_message)
                    }
                }
            }
        })

        tietCheckPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! >= tmpPassword.length) {
                    if (tmpPassword == s.toString()) {
                        tietCheckPassword.error = null
                        tilCheckPassword.error =
                            getString(R.string.sign_up_til_password_check_helper_verified_password_message)
                        password = s.toString()

                        val allFieldsFilled =
                            userEmail.isNotEmpty() && nickname.isNotEmpty() && password.isNotEmpty()
                        btnSignup.isEnabled = allFieldsFilled
                        btnSignup.isClickable = allFieldsFilled
                    } else {
                        tietCheckPassword.error =
                            getString(R.string.sign_up_til_password_error_message)
                        tilCheckPassword.error = null
                        btnSignup.apply {
                            isEnabled = false
                            isClickable = false
                        }
                    }
                } else {
                    tilCheckPassword.error = null
                    btnSignup.apply {
                        isEnabled = false
                        isClickable = false
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}