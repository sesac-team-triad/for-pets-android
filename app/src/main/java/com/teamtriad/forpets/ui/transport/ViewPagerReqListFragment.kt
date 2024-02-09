package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentViewPagerReqListBinding
import com.teamtriad.forpets.ui.transport.adapter.ReqListRecyclerViewAdapter
import com.teamtriad.forpets.util.formatDate
import com.teamtriad.forpets.util.formatDateWithYear
import com.teamtriad.forpets.util.setSafeOnClickListener
import com.teamtriad.forpets.viewmodel.TransportViewModel
import java.util.Calendar
import java.util.TimeZone

class ViewPagerReqListFragment : Fragment() {

    private val transportViewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentViewPagerReqListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

    private val recyclerViewAdapter by lazy { ReqListRecyclerViewAdapter() }

    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerReqListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transportViewModel.clearAllSelectedLocations()

        dateRangePicker = setDatePicker()

        if (transportViewModel.reqAnimalChoice.isEmpty()) {
            transportViewModel.setReqAnimalChoice(
                resources.getStringArray(R.array.req_animal_choice)
                    .toList()
            )
        }

        with(binding) {
            setRecyclerView()
            setFilteringEditTexts()
            setButtons()
        }
    }

    private fun setDatePicker(): MaterialDatePicker<Pair<Long, Long>> {
        val today: Long = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        val janThisYear = calendar.timeInMillis

        calendar[Calendar.YEAR] += 1
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decNextYear = calendar.timeInMillis

        return MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(R.string.date_picker_title)
            .setSelection(Pair(today, today))
            .setTheme(R.style.Pet_DatePicker_CalendarSmall)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(janThisYear)
                    .setEnd(decNextYear)
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    startDate = it.first.formatDateWithYear()
                    endDate = it.second.formatDateWithYear()

                    binding.tietDate.setText(
                        if (it.first == it.second) startDate
                        else "${it.first.formatDate()} - ${it.second.formatDate()}"
                    )
                }
            }
    }

    private fun navigateToLocationPickerDialog(isFrom: Boolean) {
        val action =
            TransportListsFragmentDirections.actionTransportListsFragmentToLocationPickerDialogFragment(
                isFrom
            )

        findNavController().navigate(action)
    }

    private fun FragmentViewPagerReqListBinding.setRecyclerView() {
        rvReqList.adapter = recyclerViewAdapter.apply {
            transportViewModel.transportReqMap.observe(viewLifecycleOwner) {
                submitList(
                    transportViewModel.filterTransportReqMapToList(
                        startDate, endDate,
                        actvAnimalChoice.text.toString(),
                        tietFrom.text.toString(),
                        tietTo.text.toString()
                    )
                )
            }
        }

        transportViewModel.getAllTransportReqMap()
    }

    private fun FragmentViewPagerReqListBinding.setFilteringEditTexts() {
        tietDate.setSafeOnClickListener {
            dateRangePicker.show(childFragmentManager, null)
        }
        tietDate.doOnTextChanged { text, _, _, _ ->
            (rvReqList.adapter as ReqListRecyclerViewAdapter).submitList(
                transportViewModel.filterTransportReqMapToList(
                    startDate, endDate,
                    actvAnimalChoice.text.toString(),
                    tietFrom.text.toString(),
                    tietTo.text.toString()
                )
            )
        }

        actvAnimalChoice.doOnTextChanged { text, _, _, _ ->
            (rvReqList.adapter as ReqListRecyclerViewAdapter).submitList(
                transportViewModel.filterTransportReqMapToList(
                    startDate, endDate,
                    text.toString(),
                    tietFrom.text.toString(),
                    tietTo.text.toString()
                )
            )
        }

        tietFrom.setSafeOnClickListener {
            root.requestFocus()

            navigateToLocationPickerDialog(true)
        }
        with(transportViewModel) {
            selectedFromDistrict.observe(viewLifecycleOwner) {
                if (it.isEmpty() ||
                    "${selectedFromCounty.value} ${it}" == tietFrom.text.toString()
                ) return@observe

                tietFrom.setText("${selectedFromCounty.value} ${it}")
            }

            tietFrom.doOnTextChanged { text, _, _, _ ->
                (rvReqList.adapter as ReqListRecyclerViewAdapter).submitList(
                    filterTransportReqMapToList(
                        startDate, endDate,
                        actvAnimalChoice.text.toString(),
                        text.toString(),
                        tietTo.text.toString()
                    )
                )
            }
        }

        tietTo.setSafeOnClickListener {
            root.requestFocus()

            navigateToLocationPickerDialog(false)
        }
        with(transportViewModel) {
            selectedToDistrict.observe(viewLifecycleOwner) {
                if (it.isEmpty() ||
                    "${selectedToCounty.value} ${it}" == tietTo.text.toString()
                ) return@observe

                tietTo.setText("${selectedToCounty.value} ${it}")
            }

            tietTo.doOnTextChanged { text, _, _, _ ->
                (rvReqList.adapter as ReqListRecyclerViewAdapter).submitList(
                    filterTransportReqMapToList(
                        startDate, endDate,
                        actvAnimalChoice.text.toString(),
                        tietFrom.text.toString(),
                        text.toString()
                    )
                )
            }
        }
    }

    private fun FragmentViewPagerReqListBinding.setButtons() {
        efabRegisterReq.setSafeOnClickListener {
            findNavController().navigate(R.id.action_transportListsFragment_to_transportReqFragment)
        }
        rvReqList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    efabRegisterReq.extend()
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    efabRegisterReq.shrink()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}