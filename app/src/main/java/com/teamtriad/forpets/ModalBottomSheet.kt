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

        binding.mbtSave.setOnClickListener {
            Location.sendLocationData(selectedCounty, selectedDistrict)
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
            }
            actvDistrict.setOnItemClickListener { _, _, _, _ ->
                selectedDistrict = tilBottomDistrict.editText?.text.toString()
            }
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}