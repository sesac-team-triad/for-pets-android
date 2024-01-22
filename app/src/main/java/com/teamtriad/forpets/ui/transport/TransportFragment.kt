package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportBinding

class TransportFragment : Fragment() {

    private var _binding: FragmentTransportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.efabTransportReq.setOnClickListener {
            findNavController().navigate(R.id.action_transportFragment_to_transportReqFragment)
        }
        binding.mbtgVolunteer.setOnClickListener {
            findNavController().navigate(R.id.action_transportFragment_to_transportVolFragment)
        }
        binding.btnMove.setOnClickListener {
            findNavController().navigate(R.id.action_transportFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}