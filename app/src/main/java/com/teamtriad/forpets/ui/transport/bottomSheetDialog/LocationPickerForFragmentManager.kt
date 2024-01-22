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

class LocationPickerForFragmentManager : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLocationBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedCounty: String
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
        getCountyData()

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

    private fun getCountyData() {
        with(binding) {
            actvCounty.setOnItemClickListener { _, _, _, _ ->
                selectedCounty = tilBottomCounty.editText?.text.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "OnlyCounty"
    }
}