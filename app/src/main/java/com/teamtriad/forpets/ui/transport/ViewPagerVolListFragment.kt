package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentViewPagerVolListBinding
import com.teamtriad.forpets.model.tmp.Volunteers
import com.teamtriad.forpets.ui.transport.adapter.VolListRecyclerViewAdapter
import com.teamtriad.forpets.util.formatDate
import com.teamtriad.forpets.util.formatDateWithYear
import com.teamtriad.forpets.util.setSafeOnClickListener
import com.teamtriad.forpets.viewmodel.TransportViewModel
import java.util.Calendar
import java.util.TimeZone

class ViewPagerVolListFragment : Fragment() {

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentViewPagerVolListBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerVolListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePicker()
        setScrollListener()
        setRecyclerview()
        setOnClickListener()

        transportViewModel.clearAllSelectedLocations()

        with(binding) {
            transportViewModel.selectedFromCounty.observe(viewLifecycleOwner) {
                tietFrom.setText(it)
            }

            transportViewModel.selectedToCounty.observe(viewLifecycleOwner) {
                tietTo.setText(it)
            }
        }
    }

    private fun setScrollListener() {
        with(binding) {
            binding.nsvVolList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > oldScrollY) {
                    efabVolList.shrink()
                } else {
                    efabVolList.extend()
                }
            }
        }
    }

    private fun setRecyclerview() {
        val volList = Volunteers.loadList()
        val recyclerview = binding.rvVolList
        val adapter = VolListRecyclerViewAdapter()
        adapter.submitList(volList)
        recyclerview.adapter = adapter
    }

    private fun setOnClickListener() {
        with(binding) {
            tietDate.setSafeOnClickListener {
                showDatePicker()
            }

            tietFrom.setSafeOnClickListener {
                val action = TransportListsFragmentDirections
                    .actionTransportListsFragmentToLocationPickerDialogFragment(
                        isFrom = true,
                        onlyCounty = true
                    )

                findNavController().navigate(action)
            }

            tietTo.setSafeOnClickListener {
                val action = TransportListsFragmentDirections
                    .actionTransportListsFragmentToLocationPickerDialogFragment(
                        isFrom = true,
                        onlyCounty = true
                    )

                findNavController().navigate(action)
            }

            efabVolList.setSafeOnClickListener {
                findNavController()
                    .navigate(R.id.action_transportListsFragment_to_transportVolFragment)
            }
        }
    }

    private fun setDatePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        val janThisYear = calendar.timeInMillis

        calendar[Calendar.YEAR] += 1
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decNextYear = calendar.timeInMillis

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(janThisYear)
                .setEnd(decNextYear)
                .setValidator(DateValidatorPointForward.now())

        dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(getString(R.string.date_picker_title))
            .setSelection(
                Pair(
                    MaterialDatePicker.todayInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTheme(R.style.Pet_DatePicker_CalendarSmall)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private fun showDatePicker() {
        dateRangePicker.show(requireActivity().supportFragmentManager, "volList")
        addDatePickerButtonClickListener()
    }

    private fun addDatePickerButtonClickListener() {
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val startDateText = startDate.formatDate()
            val endDateText = endDate.formatDate()

            val selectedDate = if (startDateText == endDateText) {
                startDate.formatDateWithYear()
            } else {
                "$startDateText - $endDateText"
            }
            binding.tietDate.text =
                Editable.Factory.getInstance().newEditable(selectedDate)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}