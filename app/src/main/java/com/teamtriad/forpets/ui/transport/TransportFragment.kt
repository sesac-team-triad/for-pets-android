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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.clustering.ClusterManager
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.FragmentTransportBinding
import com.teamtriad.forpets.model.tmp.Places
import com.teamtriad.forpets.ui.transport.marker.CustomClusterManager
import com.teamtriad.forpets.ui.transport.marker.MarkerItem

class TransportFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTransportBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<MarkerItem>

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
            .add(R.id.fcb_map, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setUpCluster()
    }

    private fun setUpCluster() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(37.560, 127.064), 6.5f)
        )

        clusterManager = CustomClusterManager(requireContext(), map, binding)

        addItems()
        setClusterClickListeners()
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
                        R.id.btn_request -> {
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

    private fun setClusterClickListeners() {
        with(clusterManager) {
            setOnClusterClickListener {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 10f))
                map.moveCamera(CameraUpdateFactory.newLatLng(it.position))
                true
            }

            setOnClusterItemClickListener {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 13f))
                map.moveCamera(CameraUpdateFactory.newLatLng(it.position))
                true
            }
        }

        with(map) {
            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
            setOnCameraMoveListener {
                binding.efabTransportReq.shrink()
                binding.efabTransportVol.shrink()
            }
        }
    }

    private fun addItems() {
        val list = Places.getMarkerData()

        list.forEachIndexed { index, it ->
            val offsetItem = MarkerItem(it.place, it.title, "$index")
            clusterManager.addItem(offsetItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}