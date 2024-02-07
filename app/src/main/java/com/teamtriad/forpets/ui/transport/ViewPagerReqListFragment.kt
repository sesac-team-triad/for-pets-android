package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.teamtriad.forpets.databinding.FragmentViewPagerReqListBinding
import com.teamtriad.forpets.ui.transport.adapter.ReqListRecyclerViewAdapter
import com.teamtriad.forpets.viewmodel.TransportViewModel

class ViewPagerReqListFragment : Fragment() {

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentViewPagerReqListBinding? = null
    private val binding get() = _binding!!

    private val recyclerViewAdapter by lazy { ReqListRecyclerViewAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerReqListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            setRecyclerView()
            setFilteringEditTexts()
        }
    }

    private fun navigateToLocationPickerDialog(onlyCounty: Boolean) {
        val action =
            TransportListsFragmentDirections.actionTransportListsFragmentToLocationPickerDialogFragment(
                onlyCounty
            )

        findNavController().navigate(action)
    }

    private fun FragmentViewPagerReqListBinding.setRecyclerView() {
        rvReqList.adapter = recyclerViewAdapter.apply {
            transportViewModel.transportReqMap.observe(viewLifecycleOwner) {
                submitList(it.map { it.value })
            }
        }

        transportViewModel.getAllTransportReqMap()
    }

    private fun FragmentViewPagerReqListBinding.setFilteringEditTexts() {
        tietFrom.setOnClickListener {
            root.requestFocus()

            navigateToLocationPickerDialog(false)
        }
        tietTo.setOnClickListener {
            root.requestFocus()

            navigateToLocationPickerDialog(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}