package com.teamtriad.forpets.ui.transport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportBinding
import com.teamtriad.forpets.model.tmp.Markers
import com.teamtriad.forpets.model.tmp.Places

class TransportFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTransportBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapFragment()
        setOnClickListeners()
    }

    private fun setMapFragment() {
        val mapFragment = SupportMapFragment.newInstance()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.map, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)
    }

    private fun setOnClickListeners() {
        with(binding) {
            efabTransportReq.setOnClickListener {
                findNavController().navigate(R.id.action_transportFragment_to_transportReqFragment)
            }

            efabTransportVol.setOnClickListener {
                findNavController().navigate(R.id.action_transportFragment_to_transportVolFragment)
            }

            mbtgTransport.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.mbtg_request -> {
                            efabTransportReq.visibility = View.VISIBLE
                            efabTransportVol.visibility = View.GONE
                        }

                        else -> {
                            efabTransportVol.visibility = View.VISIBLE
                            efabTransportReq.visibility = View.GONE
                        }
                    }
                }
            }

            ibPaw.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.transport_dialog_title))
                    .setMessage(getString(R.string.transport_dialog_message))
                    .setPositiveButton(getString(R.string.transport_dialog_confirm_btn)) { dialog, _ ->
                        val bnv = requireActivity().findViewById<BottomNavigationView>(R.id.bnv)
                        bnv.selectedItemId = R.id.adoptFragment
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.transport_dialog_cancel_btn)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val sesac = LatLng(37.560, 127.064)
        map = googleMap
        with(map) {
            moveCamera(CameraUpdateFactory.zoomTo(6.5f))
            moveCamera(CameraUpdateFactory.newLatLng(sesac))

            addMarker(Places.getMarkerData())
            setClickListeners()
        }
    }

    private fun addMarker(list: List<Markers>) {
        list.forEach { marker ->
            val markerOptions = MarkerOptions()
                .position(marker.place)
                .title(marker.title)
            map.addMarker(markerOptions)!!
        }
    }

    private fun setClickListeners() {
        with(map) {
            setOnCameraMoveListener {
                binding.efabTransportReq.shrink()
            }

            setOnCameraIdleListener {
                binding.efabTransportReq.extend()
            }

            setOnMarkerClickListener {
                moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 13f))
                moveCamera(CameraUpdateFactory.newLatLng(it.position))
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}