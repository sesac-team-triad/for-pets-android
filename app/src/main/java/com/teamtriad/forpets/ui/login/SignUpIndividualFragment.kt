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
import com.google.android.material.snackbar.Snackbar
import com.teamtriad.forpets.BuildConfig.EMAIL_ADDRESS
import com.teamtriad.forpets.BuildConfig.EMAIL_PASSOWRD
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentSignUpIndividualBinding
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

    private var _binding: FragmentSignUpIndividualBinding? = null
    private val binding get() = _binding!!

    private var isValidEmail = false
    private lateinit var authCode: String

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

    }

    private fun setOnClickListener() = with(binding) {
        btnSendAuthCode.setOnClickListener {
            if (isValidEmail) {
                authCode = sendEmail(tietEmail.text.toString())

                val softKeyboard =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)

                Snackbar.make(
                    requireView(),
                    R.string.sign_up_email_transport_complete,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendEmail(userEmail: String): String {
        val email = EMAIL_ADDRESS
        val password = EMAIL_PASSOWRD
        val code = (100..10000).random().toString()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}