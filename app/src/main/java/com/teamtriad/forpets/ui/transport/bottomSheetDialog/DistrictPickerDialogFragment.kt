package com.teamtriad.forpets.ui.transport.bottomSheetDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.District
import com.teamtriad.forpets.databinding.BottomSheetVolDistrictBinding
import com.teamtriad.forpets.viewmodel.TransportViewModel

class DistrictPickerDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: TransportViewModel by activityViewModels()

    private var _binding: BottomSheetVolDistrictBinding? = null
    private val binding get() = _binding!!
    private var selectedCounty: String = ""
    private lateinit var selectedDistrict: String
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var outerMap: Map<String, Map<String, District>>
    private lateinit var counties: List<String>
    private lateinit var districts: MutableList<String>

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
        viewModel.getAllCountyMap()
        outerMap = viewModel.locationMap.value!!
        counties = outerMap.keys.toList()
        Log.d("cc", "${counties.forEach { println(it) }}")

        setData()
        getData()

        binding.btnSave.setOnClickListener {
        }
    }

    private fun setData() {
        if (viewModel.selectedData.isNotEmpty()) {
            Log.d("bb", viewModel.selectedData)
            val newDataIndex = counties.indexOf(viewModel.selectedData)
            val newData = counties.toTypedArray()
            binding.tilBottomCounty.hint = newData[newDataIndex]
        }
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            counties.toTypedArray()
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
//    counties.forEach { outerMap[it]?.let { innerMap -> districts.addAll(innerMap.keys) } }
    private fun getData() {
        with(binding) {
            actvCounty.setOnItemClickListener { _, _, _, _ ->
                selectedCounty = tilBottomCounty.editText?.text.toString()
                viewModel.selectedData = selectedCounty
                Log.d("aa", viewModel.selectedData)
                val cities = outerMap[selectedCounty]?.keys?.toTypedArray()
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

    companion object {
        const val ONLY_COUNTY = true
    }

}