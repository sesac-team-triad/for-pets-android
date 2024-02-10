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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.databinding.FragmentTransportReqBinding
import com.teamtriad.forpets.util.formatDate
import com.teamtriad.forpets.util.formatDateWithYear
import com.teamtriad.forpets.util.setSafeOnClickListener
import com.teamtriad.forpets.viewmodel.TransportViewModel
import java.util.Calendar
import java.util.TimeZone

class TransportReqFragment : Fragment() {

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentTransportReqBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

    private lateinit var fromLocation: String
    private lateinit var toLocation: String

    private lateinit var startDate: String
    private lateinit var endDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportReqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePicker()
        setPhotoPicker()
        setOnClickListener()
        checkButtonEnabled()
        makeEditTextBigger()

        transportViewModel.clearAllSelectedLocations()

        with(transportViewModel) {
            selectedFromDistrict.observe(viewLifecycleOwner) {
                if (it.isEmpty() ||
                    "${selectedFromCounty.value} $it" == binding.tietFrom.text.toString()
                ) return@observe

                fromLocation = "${selectedFromCounty.value} $it"
                binding.tietFrom.setText(fromLocation)
            }

            selectedToDistrict.observe(viewLifecycleOwner) {
                if (it.isEmpty() ||
                    "${selectedToCounty.value} $it" == binding.tietTo.text.toString()
                ) return@observe

                toLocation = "${selectedToCounty.value} $it"
                binding.tietTo.setText(toLocation)
            }
        }
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
            tietDate.setSafeOnClickListener {
                showDatePicker()
            }

            tietFrom.setSafeOnClickListener {
                val action = TransportReqFragmentDirections
                    .actionTransportReqFragmentToLocationPickerDialogFragment(true)

                findNavController().navigate(action)
            }

            tietTo.setSafeOnClickListener {
                val action = TransportReqFragmentDirections
                    .actionTransportReqFragmentToLocationPickerDialogFragment(false)

                findNavController().navigate(action)
            }

            sivPetImage.setSafeOnClickListener {
                pickMultipleMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            btnPost.setSafeOnClickListener {
                sendRequestData(makeRequestData())
                findNavController().navigate(R.id.action_transportReqFragment_to_transportListsFragment)
            }
        }
    }

    private fun sendRequestData(reqData: TransportReq) {
        transportViewModel.addTransportReq(reqData)
    }

    private fun makeRequestData(): TransportReq = with(binding) {
        return TransportReq(
            uid = "textReq1",
            title = "봉사요청 합니다",
            startDate = startDate,
            endDate = endDate,
            animal = actvAnimal.text.toString(),
            from = fromLocation,
            to = toLocation,
            name = etName.text.toString(),
            age = etAge.text.toString(),
            weight = etWeight.text.toString(),
            kind = etKind.text.toString(),
            characterCaution = etCharacterCaution.text.toString(),
            message = etMessage.text.toString()
        )
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

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            }
            tietTitle.addTextChangedListener(textWatcher)
            tietDate.addTextChangedListener(textWatcher)
            tietFrom.addTextChangedListener(textWatcher)
            tietTo.addTextChangedListener(textWatcher)
            etName.addTextChangedListener(textWatcher)
            etAge.addTextChangedListener(textWatcher)
            etWeight.addTextChangedListener(textWatcher)
            etKind.addTextChangedListener(textWatcher)
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
                    && !tietFrom.text.isNullOrEmpty()
                    && !tietTo.text.isNullOrEmpty()
                    && !etName.text.isNullOrEmpty()
                    && !etAge.text.isNullOrEmpty()
                    && !etWeight.text.isNullOrEmpty()
                    && !etKind.text.isNullOrEmpty()
                    && !etCharacterCaution.text.isNullOrEmpty()
                    && !etMessage.text.isNullOrEmpty()
                    && sivPetImage.drawable != null

            btnPost.apply {
                isEnabled = allFieldsFilled
                isCheckable = allFieldsFilled
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
        dateRangePicker.show(requireActivity().supportFragmentManager, "req")
        addDatePickerButtonClickListener()
    }

    private fun addDatePickerButtonClickListener() {
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            startDate = selection.first.formatDate()
            endDate = selection.second.formatDate()

            val selectedDate = if (startDate == endDate) {
                selection.first.formatDateWithYear()
            } else {
                "$startDate - $endDate"
            }
            binding.tietDate.text =
                Editable.Factory.getInstance().newEditable(selectedDate)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}