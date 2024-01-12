package com.teamtriad.forpets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.teamtriad.forpets.databinding.FragmentTransportBinding

class TransportFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var _binding: FragmentTransportBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val seoul = LatLng(37.5642135, 127.0016985)
        mMap.addMarker(
            MarkerOptions()
                .position(seoul)
                .title("Marker in Seoul")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapview.onCreate(savedInstanceState)
        binding.mapview.getMapAsync(this)
    }


    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapview.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapview.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        binding.mapview.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapview.onSaveInstanceState(outState)
    }

}