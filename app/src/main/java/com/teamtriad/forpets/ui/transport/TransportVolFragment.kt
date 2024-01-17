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
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamtriad.forpets.ModalBottomSheet
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportVolBinding
import com.teamtriad.forpets.util.formatDate
import java.util.Calendar
import java.util.TimeZone


class TransportVolFragment : Fragment() {

    private var _binding: FragmentTransportVolBinding? = null
    private val binding get() = _binding!!
    private lateinit var textWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportVolBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        makeEditTextBigger()
        checkButtonEnabled()
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

            mbPost.setOnClickListener {
                findNavController().navigate(R.id.action_transportVolFragment_to_transportListsFragment)
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
            .setTheme(R.style.Pet_DatePicker_CalendarSmall)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
        dateRangePicker.show(requireActivity().supportFragmentManager, "tag")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val startDateText = startDate.formatDate()
            val endDateText = endDate.formatDate()

            val selectedText = "$startDateText - $endDateText"
            binding.tietDate.text =
                Editable.Factory.getInstance().newEditable(selectedText)
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
            tietFromDetail.addTextChangedListener(textWatcher)
            tietToDetail.addTextChangedListener(textWatcher)
            tietMessage.addTextChangedListener(textWatcher)
        }
    }

    private fun checkFieldsAndEnableButton() {
        with(binding) {
            val allFieldsFilled = !tietTitle.text.isNullOrEmpty()
                    && !tietDate.text.isNullOrEmpty()
//                    && !tietReqFrom.text.isNullOrEmpty()
//                    && !tietReqTo.text.isNullOrEmpty()
                    && !tietFromDetail.text.isNullOrEmpty()
                    && !tietToDetail.text.isNullOrEmpty()
                    && !tietMessage.text.isNullOrEmpty()
            Log.d("ab", "$allFieldsFilled")

            mbPost.apply {
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