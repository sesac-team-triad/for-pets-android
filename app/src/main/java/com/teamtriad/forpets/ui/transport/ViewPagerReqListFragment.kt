package com.teamtriad.forpets.ui.transport

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.teamtriad.forpets.databinding.FragmentViewPagerReqListBinding

class ViewPagerReqListFragment : Fragment() {

    private var _binding: FragmentViewPagerReqListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerReqListBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tietFrom.setOnClickListener {
                root.requestFocus()

                navigateToLocationPickerDialog(false)
            }
            tietTo.setOnClickListener {
                root.requestFocus()

                navigateToLocationPickerDialog(false)
            }
        }
    }

    private fun navigateToLocationPickerDialog(onlyCounty: Boolean) {
        val action =
            TransportListsFragmentDirections.actionTransportListsFragmentToLocationPickerDialogFragment(
                onlyCounty
            )

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}