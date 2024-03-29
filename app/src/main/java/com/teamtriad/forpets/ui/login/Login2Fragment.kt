package com.teamtriad.forpets.ui.login

import android.content.Context
import android.os.Bundle
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
    }

    private fun setOnClickListeners() {
        with(binding) {
            root.setOnClickListener {
                root.clearFocus()
                val softKeyboard =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                softKeyboard.hideSoftInputFromWindow(root.windowToken, 0)
            }

            tvSignup1.setOnClickListener {
                findNavController().navigate(R.id.action_login2Fragment_to_signUpIndividualFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}