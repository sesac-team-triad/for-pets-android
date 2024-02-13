package com.teamtriad.forpets.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import com.teamtriad.forpets.databinding.DialogScheduleInputBinding

class ScheduleInputDialog(
    context: Context,
    private val onScheduleInputListener: OnScheduleInputListener
) :
    AppCompatDialog(context) {

    private lateinit var binding: DialogScheduleInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogScheduleInputBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.btnCancle.setOnClickListener {
            dismiss()
        }

        binding.btnOk.setOnClickListener {
            val dogName = binding.etAnimalName.text.toString()
            val departureLocation = binding.etFrom.text.toString()
            val destinationLocation = binding.etTo.text.toString()

            onScheduleInputListener.onInputReceived(dogName, departureLocation, destinationLocation)
            dismiss()
        }
    }

    interface OnScheduleInputListener {
        fun onInputReceived(dogName: String, departureLocation: String, destinationLocation: String)
    }
}
