package com.teamtriad.forpets.ui.transport.bottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.BottomSheetLocationBinding
import com.teamtriad.forpets.model.tmp.Location

class LocationPickerForNavigation : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLocationBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedCounty: String
    private lateinit var selectedDistrict: String
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var counties: Map<String, List<String>>

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
        counties = Location.loadLocationMap()

        setData()
        getData()

        binding.btnSave.setOnClickListener {
        }
    }

    private fun setData() {
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            counties.keys.toTypedArray()
        )
        binding.actvCounty.setAdapter(adapter)
    }

    private fun getData() {
        with(binding) {
            actvCounty.setOnItemClickListener { _, _, _, _ ->
                selectedCounty = tilBottomCounty.editText?.text.toString()
                val cities = counties[selectedCounty]?.toTypedArray()
                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    cities!!
                )
                actvDistrict.setAdapter(adapter)
                tilBottomDistrict.isEnabled = true
                actvDistrict.isEnabled = true
            }
            actvDistrict.setOnItemClickListener { _, _, _, _ ->
                selectedDistrict = tilBottomDistrict.editText?.text.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}