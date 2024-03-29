package com.teamtriad.forpets.ui.transport.bottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.District
import com.teamtriad.forpets.databinding.BottomSheetLocationBinding
import com.teamtriad.forpets.util.setSafeOnClickListener
import com.teamtriad.forpets.viewmodel.TransportViewModel

class LocationPickerDialogFragment : BottomSheetDialogFragment() {

    private val args: LocationPickerDialogFragmentArgs by navArgs()

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: BottomSheetLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var locationMap: Map<String, Map<String, District>>

    private var selectedCounty: String = ""
    private var selectedDistrict: String = ""


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
        locationMap = transportViewModel.locationMap.value!!

        if (args.onlyCounty) {
            setData()
            getCountyData()
        } else {
            setData()
            getData()
        }

        binding.btnSave.setSafeOnClickListener {
            if (args.isFrom) {
                transportViewModel.setSelectedFromCounty(selectedCounty)
                transportViewModel.setSelectedFromDistrict(selectedDistrict)
            } else {
                transportViewModel.setSelectedToCounty(selectedCounty)
                transportViewModel.setSelectedToDistrict(selectedDistrict)
            }

            findNavController().popBackStack()
        }
    }

    private fun setData() {
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            locationMap.keys.toTypedArray()
        )
        binding.actvCounty.setAdapter(adapter)
    }

    private fun getCountyData() {
        with(binding) {
            actvCounty.setOnItemClickListener { parent, _, position, _ ->
                selectedCounty = parent.getItemAtPosition(position) as String
                btnSave.isEnabled = true
            }
        }
    }

    private fun getData() {
        with(binding) {
            actvCounty.setOnItemClickListener { parent, _, position, _ ->
                btnSave.isEnabled = false
                selectedCounty = parent.getItemAtPosition(position) as String
                val districts = locationMap[selectedCounty]!!.keys.toTypedArray()
                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    districts
                )
                actvDistrict.setAdapter(adapter)
                tilBottomDistrict.isEnabled = true
                actvDistrict.isEnabled = true
            }
            actvDistrict.setOnItemClickListener { parent, _, position, _ ->
                selectedDistrict = parent.getItemAtPosition(position) as String
                btnSave.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}