package com.teamtriad.forpets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtriad.forpets.databinding.BottomSheetLocationBinding
import com.teamtriad.forpets.tmp.Location

class ModalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counties = Location.loadLocationMap()
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            counties.keys.toTypedArray()
        )
        with(binding) {
            actvCounty.setAdapter(adapter)
            actvCounty.setOnItemClickListener { _, _, _, _ ->
                val selectedCounty = binding.tilBottomCounty.editText?.text.toString()
                val cities = counties[selectedCounty]?.toTypedArray()
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    cities!!
                )
                actvDistrict.setAdapter(adapter)
            }
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}