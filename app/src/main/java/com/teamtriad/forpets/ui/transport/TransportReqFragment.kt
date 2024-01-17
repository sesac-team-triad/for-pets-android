package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportReqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPhotoPicker()
        setOnClickListener()
        checkButtonEnabled()
        makeEditTextBigger()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setPhotoPicker() {
        binding.tvPhotoCount.text = getString(R.string.all_photo_count, 0)
        pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                    with(binding) {
                        sivPetImage.setImageURI(uris[0])
                        tvPhotoCount.text = getString(R.string.all_photo_count, uris.size)
                        ivAddImage.visibility = View.GONE
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
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

            sivPetImage.setOnClickListener {
                pickMultipleMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
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
            etName.addTextChangedListener(textWatcher)
            etAge.addTextChangedListener(textWatcher)
            etWeight.addTextChangedListener(textWatcher)
            etBreed.addTextChangedListener(textWatcher)
            etCharacterCaution.addTextChangedListener(textWatcher)
            etMessage.addTextChangedListener(textWatcher)

            sivPetImage.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                checkFieldsAndEnableButton()
            }
        }
    }

    private fun checkFieldsAndEnableButton() {
        with(binding) {
            val allFieldsFilled = !tietTitle.text.isNullOrEmpty()
                    && !tietDate.text.isNullOrEmpty()
//                    && !tietReqFrom.text.isNullOrEmpty()
//                    && !tietReqTo.text.isNullOrEmpty()
                    && !etName.text.isNullOrEmpty()
                    && !etAge.text.isNullOrEmpty()
                    && !etWeight.text.isNullOrEmpty()
                    && !etBreed.text.isNullOrEmpty()
                    && !etCharacterCaution.text.isNullOrEmpty()
                    && !etMessage.text.isNullOrEmpty()
                    && sivPetImage.drawable != null

            mbPost.apply {
                isEnabled = allFieldsFilled
                isCheckable = allFieldsFilled
            }
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

    private fun makeEditTextBigger() {
        val initialLines = 4

        with(binding) {
            etCharacterCaution.addTextChangedListener(object : TextWatcher {
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
                    val lineCount = etCharacterCaution.lineCount
                    val newMaxLines = if (lineCount > initialLines) {
                        lineCount + 1
                    } else {
                        initialLines
                    }
                    etCharacterCaution.maxLines = newMaxLines
                }
            })

            etMessage.addTextChangedListener(object : TextWatcher {
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
                    val lineCount = etMessage.lineCount
                    val newMaxLines = if (lineCount > initialLines) {
                        lineCount + 1
                    } else {
                        initialLines
                    }
                    etMessage.maxLines = newMaxLines
                }
            })
        }
    }
}