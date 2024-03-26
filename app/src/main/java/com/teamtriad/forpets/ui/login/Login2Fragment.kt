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
import androidx.navigation.fragment.findNavController
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentLogin2Binding

class Login2Fragment : Fragment() {

    private var _binding: FragmentLogin2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogin2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        checkButtonEnabled()
    }

    private fun setOnClickListeners() {
        with(binding) {
            layout.setOnClickListener {
                layout.clearFocus()
                val softKeyboard =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)
            }

            tvSignup1.setOnClickListener {
                findNavController().navigate(R.id.action_login2Fragment_to_signUpIndividualFragment)
            }
        }
    }

    private fun checkButtonEnabled() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                checkFieldsAndEnableButton()
            }
        }

        with(binding) {
            tietEmail.addTextChangedListener(textWatcher)
            tietPassword.addTextChangedListener(textWatcher)
        }
    }

    private fun checkFieldsAndEnableButton() = with(binding) {
        val allFieldsFilled = !tietEmail.text.isNullOrEmpty() && !tietPassword.text.isNullOrEmpty()

        btnLogin.apply {
            isEnabled = allFieldsFilled
            isClickable = allFieldsFilled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}