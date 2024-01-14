package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamtriad.forpets.ModalBottomSheet
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportReqBinding
import com.teamtriad.forpets.util.formatDate
import java.util.Calendar
import java.util.TimeZone

class TransportReqFragment : Fragment() {

    private var _binding: FragmentTransportReqBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportReqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tietReqDate.setOnClickListener {
                showDatePicker()
            }

            tietReqFrom.setOnClickListener {
                showModalBottomSheet()
            }

            tietReqTo.setOnClickListener {
                showModalBottomSheet()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showModalBottomSheet() {
        val bottomSheet = ModalBottomSheet()
        bottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheet.TAG)
    }

    private fun showDatePicker() {
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

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("출발일 - 도착일")
            .setSelection(
                Pair(
                    MaterialDatePicker.todayInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTheme(R.style.Pet_DatePickerCalendar_Small)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val startDateText = startDate.formatDate()
            val endDateText = endDate.formatDate()

            val selectedText = "$startDateText - $endDateText"
            binding.tietReqDate.text =
                Editable.Factory.getInstance().newEditable(selectedText)
        }
    }

}