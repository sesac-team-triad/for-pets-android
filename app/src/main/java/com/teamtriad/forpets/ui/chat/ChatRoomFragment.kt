package com.teamtriad.forpets.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamtriad.forpets.databinding.FragmentChatRoomBinding


class ChatRoomFragment : Fragment() {
    private val args: ChatRoomFragmentArgs by navArgs()
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()

        binding.toolbar.setNavigationOnClickListener {
            onBackButtonPress()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackButtonPress()
        }

    }
    private fun initializeView() {
        binding.toolbar.subtitle = args.nickname
        requireActivity().window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
    }
    private fun onBackButtonPress() {
        requireActivity().window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


