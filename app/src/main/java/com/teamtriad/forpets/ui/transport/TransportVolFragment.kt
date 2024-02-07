package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.teamtriad.forpets.databinding.FragmentTransportVolBinding
import com.teamtriad.forpets.util.formatDate
import com.teamtriad.forpets.util.formatDateWithYear
import com.teamtriad.forpets.util.setSafeOnClickListener
import com.teamtriad.forpets.viewmodel.TransportViewModel
import java.util.Calendar
import java.util.TimeZone

class TransportVolFragment : Fragment() {

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentTransportVolBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportVolBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePicker()
        setOnClickListener()
        makeEditTextBigger()
        checkButtonEnabled()

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

    private fun setOnClickListener() {
        with(binding) {
            tietDate.setSafeOnClickListener {
                showDatePicker()
            }

            tietFrom.setSafeOnClickListener {
//                val action = TransportVolFragmentDirections
//                    .actionTransportVolFragmentToLocationPickerDialogFragment(!LocationPickerDialogFragment.ONLY_COUNTY)
//
//                findNavController().navigate(action)
                findNavController().navigate(R.id.action_transportVolFragment_to_districtPickerDialogFragment)
            }

            tietTo.setSafeOnClickListener {
//                val action = TransportVolFragmentDirections
//                    .actionTransportVolFragmentToLocationPickerDialogFragment(!LocationPickerDialogFragment.ONLY_COUNTY)
//
//                findNavController().navigate(action)
                findNavController().navigate(R.id.action_transportVolFragment_to_districtPickerDialogFragment)

            }

            btnPost.setSafeOnClickListener {
                findNavController()
                    .navigate(R.id.action_transportVolFragment_to_transportListsFragment)
            }
        }
    }

    private fun makeEditTextBigger() {
        val initialLines = 6

        with(binding) {

            tietMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    val lineCount = tietMessage.lineCount
                    val newMaxLines = if (lineCount > initialLines) {
                        lineCount + 1
                    } else {
                        initialLines
                    }
                    tietMessage.maxLines = newMaxLines
                }
            })
        }
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
        dateRangePicker.show(requireActivity().supportFragmentManager, "vol")
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

    private fun checkButtonEnabled() {
        with(binding) {
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkFieldsAndEnableButton()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
            tietTitle.addTextChangedListener(textWatcher)
            tietDate.addTextChangedListener(textWatcher)
            tietFrom.addTextChangedListener(textWatcher)
            tietTo.addTextChangedListener(textWatcher)
            tietMessage.addTextChangedListener(textWatcher)
        }
    }

    private fun checkFieldsAndEnableButton() {
        with(binding) {
            val allFieldsFilled = !tietTitle.text.isNullOrEmpty()
                    && !tietDate.text.isNullOrEmpty()
//                    && !tietReqFrom.text.isNullOrEmpty()
//                    && !tietReqTo.text.isNullOrEmpty()
                    && !tietMessage.text.isNullOrEmpty()
            Log.d("ab", "$allFieldsFilled")

            btnPost.apply {
                isEnabled = allFieldsFilled
                isCheckable = allFieldsFilled
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}