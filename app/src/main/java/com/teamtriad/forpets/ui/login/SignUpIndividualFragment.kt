package com.teamtriad.forpets.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.teamtriad.forpets.BuildConfig.EMAIL_ADDRESS
import com.teamtriad.forpets.BuildConfig.EMAIL_PASSOWRD
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

    private lateinit var auth: FirebaseAuth

    private lateinit var authCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

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
    }

    private fun setOnClickListener() = with(binding) {
        btnSendAuthCode.setOnClickListener {
            if (!tietEmail.text.isNullOrEmpty()) {
                sendEmail(tietEmail.text.toString())
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
            // 구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달
            val session = Session.getInstance(props,
                object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(email, password)
                    }
                })

            // 메시지 객체 만들기
            val message = MimeMessage(session)
            message.sender = InternetAddress(email)                                                         // 보내는 사람 설정
            message.addRecipient(Message.RecipientType.TO, InternetAddress(userEmail))                      // 받는 사람 설정
            message.subject = "도와줘멍냥 회원가입 인증번호입니다."                                               // 이메일 제목
            message.setText("\n\n안녕하세요. 도와줘멍냥입니다!\n\n아래 인증번호를 인증창에 입력해주세요.\n\n인증번호 : $code")    // 이메일 내용

            // 전송
            Transport.send(message)

        }
        return code
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}