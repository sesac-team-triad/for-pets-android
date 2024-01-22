package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentViewPagerVolListBinding
import com.teamtriad.forpets.model.tmp.Volunteers
import com.teamtriad.forpets.ui.transport.adpater.VolListRecyclerViewAdapter
import com.teamtriad.forpets.ui.transport.bottomSheetDialog.LocationPickerForFragmentManager
import com.teamtriad.forpets.util.formatDate
import com.teamtriad.forpets.util.formatDateWithYear
import java.util.Calendar
import java.util.TimeZone

class ViewPagerVolListFragment : Fragment() {

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
            tietDate.setOnClickListener {
                showDatePicker()
            }

            tietFrom.setOnClickListener {
                showModalBottomSheet()
            }

            tietTo.setOnClickListener {
                showModalBottomSheet()
            }

            efabVolList.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_transportListsFragment_to_transportVolFragment)
            }
        }
    }

    private fun showModalBottomSheet() {
        val bottomSheet = LocationPickerForFragmentManager()
        bottomSheet.show(
            requireActivity().supportFragmentManager,
            LocationPickerForFragmentManager.TAG
        )
    }

    private fun setDatePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        val janThisYear = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decThisYear = calendar.timeInMillis

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(janThisYear)
                .setEnd(decThisYear)
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
        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")
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