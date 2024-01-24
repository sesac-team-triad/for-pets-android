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
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportBinding

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
        setOnClickListener()

    }

    private fun setMapFragment() {
        val mapFragment = SupportMapFragment.newInstance()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.map, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)
    }

    private fun setOnClickListener() {
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val sesac = LatLng(37.560, 127.064)
        map.addMarker(
            MarkerOptions()
                .position(sesac)
                .title("sesac")
        )
        map.setOnMarkerClickListener {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sesac, 13f))
            map.moveCamera(CameraUpdateFactory.newLatLng(sesac))
            true
        }

        map.setOnCameraMoveListener {
            binding.efabTransportReq.shrink()
        }
        map.setOnCameraIdleListener {
            binding.efabTransportReq.extend()
        }

        map.moveCamera(CameraUpdateFactory.zoomTo(6.5f))
        map.moveCamera(CameraUpdateFactory.newLatLng(sesac))
    }

    private fun addMarker(position: LatLng, title: String) {
        val markerOptions = MarkerOptions()
            .position(position)
            .title(title)

        map.addMarker(markerOptions)
    }
}