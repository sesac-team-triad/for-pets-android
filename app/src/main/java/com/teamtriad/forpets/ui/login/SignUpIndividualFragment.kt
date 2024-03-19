package com.teamtriad.forpets.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.teamtriad.forpets.BuildConfig.EMAIL_ADDRESS
import com.teamtriad.forpets.BuildConfig.EMAIL_PASSOWRD
import com.teamtriad.forpets.R
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

    private var _binding: FragmentSignUpIndividualBinding? = null
    private val binding get() = _binding!!

    private var isValidEmail = false
    private lateinit var authCode: String
    private lateinit var userEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpIndividualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        checkEmailAddress()
        checkAuthCode()
        checkUserNickname()
    }

    private fun setOnClickListener() = with(binding) {
        val softKeyboard =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        btnSendAuthCode.setSafeOnClickListener {
            if (isValidEmail) {
                userEmail = tietEmail.text.toString()
                authCode = sendEmail(userEmail)
                softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)

                Snackbar.make(
                    requireView(),
                    R.string.sign_up_email_transport_complete,
                    Snackbar.LENGTH_SHORT
                ).show()

                tietInputAuthCode.apply {
                    text?.clear()
                }

                btnAuthenticate.visibility = View.VISIBLE
                btnCompletedAuth.visibility = View.GONE
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
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}