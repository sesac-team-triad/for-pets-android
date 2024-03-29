package com.teamtriad.forpets.ui.transport.bottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.District
import com.teamtriad.forpets.databinding.BottomSheetVolDistrictBinding
import com.teamtriad.forpets.ui.transport.adapter.DistrictPickerRecyclerViewAdapter
import com.teamtriad.forpets.viewmodel.TransportViewModel

interface OnClickListener {
    fun deleteItem(district: String)
}

class DistrictPickerDialogFragment : BottomSheetDialogFragment(), OnClickListener {

    private val args: DistrictPickerDialogFragmentArgs by navArgs()

    private val viewModel: TransportViewModel by activityViewModels()

    private var _binding: BottomSheetVolDistrictBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvAdapter: DistrictPickerRecyclerViewAdapter

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var locationMap: Map<String, Map<String, District>>
    private lateinit var counties: Array<String>

    private var selectedCounty: String = ""
    private var selectedDistrict: String = ""
    private val selectedDistrictList = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetVolDistrictBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setData()
        setOnClickListeners()
    }

    private fun setData() {
        locationMap = viewModel.locationMap.value!!
        counties = locationMap.keys.toTypedArray()

        selectedCounty = if (args.isFrom) {
            viewModel.selectedFromCounty.value!!
        } else {
            viewModel.selectedToCounty.value!!
        }

        adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            counties
        )
        binding.actvCounty.setAdapter(adapter)

        if (selectedCounty.isNotEmpty()) {
            binding.actvCounty.setText(selectedCounty)
            getData(true)
        } else {
            getData(false)
        }
    }

    private fun getData(isSelected: Boolean) {
        with(binding) {
            if (isSelected) {
                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    counties
                )
                binding.actvCounty.setAdapter(adapter)

                val districts = locationMap[selectedCounty]?.keys?.toTypedArray()

                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    districts!!
                )
                actvDistrict.setAdapter(adapter)

                tilBottomDistrict.isEnabled = true
                actvDistrict.isEnabled = true

                if (args.isFrom) {
                    viewModel.selectedFromDistrictList.value?.let { selectedDistrictList.addAll(it) }
                } else {
                    viewModel.selectedToDistrictList.value?.let { selectedDistrictList.addAll(it) }
                }

                setRecyclerView()
                checkButtonEnabled()
            }

            actvCounty.setOnItemClickListener { parent, _, position, _ ->
                selectedCounty = parent.getItemAtPosition(position) as String

                val districts = locationMap[selectedCounty]?.keys?.toTypedArray()

                adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    districts!!
                )
                actvDistrict.setAdapter(adapter)

                tilBottomDistrict.isEnabled = true
                actvDistrict.isEnabled = true

                if (args.isFrom) {
                    if (selectedCounty != viewModel.selectedFromCounty.value) {
                        selectedDistrictList.clear()
                        setRecyclerView()
                    }
                } else {
                    if (selectedCounty != viewModel.selectedToCounty.value) {
                        selectedDistrictList.clear()
                        setRecyclerView()
                    }
                }

                checkButtonEnabled()
                binding.actvDistrict.text.clear()
            }

            actvDistrict.setOnItemClickListener { parent, _, position, _ ->
                selectedDistrict = parent.getItemAtPosition(position) as String

                if (selectedDistrict !in selectedDistrictList) {
                    selectedDistrictList.add(selectedDistrict)
                    setRecyclerView()
                    checkButtonEnabled()
                }
            }
        }
    }

    private fun setRecyclerView() {
        recyclerView = binding.rvDistrict
        rvAdapter = DistrictPickerRecyclerViewAdapter(this)
        rvAdapter.submitList(selectedDistrictList)
        recyclerView.adapter = rvAdapter
    }

    private fun setOnClickListeners() {
        binding.btnSave.setOnClickListener {
            if (args.isFrom) {
                viewModel.setSelectedFromCounty(selectedCounty)
                viewModel.setSelectedFromDistrictList(selectedDistrictList)
            } else {
                viewModel.setSelectedToCounty(selectedCounty)
                viewModel.setSelectedToDistrictList(selectedDistrictList)
            }

            findNavController().popBackStack()
        }
    }

    override fun deleteItem(district: String) {
        selectedDistrictList.remove(district)
        setRecyclerView()
        checkButtonEnabled()
    }

    private fun checkButtonEnabled() {
        with(binding) {

            val isRecyclerViewNotEmpty = (recyclerView.adapter?.itemCount ?: 0) > 0

            btnSave.apply {
                isEnabled = isRecyclerViewNotEmpty
                isCheckable = isRecyclerViewNotEmpty
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}