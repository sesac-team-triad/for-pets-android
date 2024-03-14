package com.teamtriad.forpets.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentSignUpIndividualBinding

class SignUpIndividualFragment : Fragment() {

    private var _binding: FragmentSignUpIndividualBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun checkEmailAddress() {
        binding.tietEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()

                if (!isValid) {
                    binding.tilEmail.error = getString(R.string.sign_up_email_error_message)
                } else {
                    binding.tilEmail.error = null
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}