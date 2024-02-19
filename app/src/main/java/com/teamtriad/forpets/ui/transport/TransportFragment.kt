package com.teamtriad.forpets.ui.transport

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.clustering.ClusterManager
import com.teamtriad.forpets.R
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.data.source.network.model.TransportVol
import com.teamtriad.forpets.databinding.FragmentTransportBinding
import com.teamtriad.forpets.ui.transport.marker.CustomClusterManager
import com.teamtriad.forpets.ui.transport.marker.Item
import com.teamtriad.forpets.ui.transport.marker.MarkerItem
import com.teamtriad.forpets.viewmodel.TransportViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

class TransportFragment : Fragment() {

    private val viewModel: TransportViewModel by activityViewModels()

    private var _binding: FragmentTransportBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<MarkerItem>
    private var segmentedButtonId: Int = R.id.btn_request

    private val itemList: MutableList<Item> = mutableListOf()
    private val latLngSet: MutableSet<Pair<Double, Double>> = mutableSetOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllTransportReqMap()
        viewModel.getAllTransportVolMap()

        setMapFragment()
        setOnClickListeners()
    }

    private fun setMapFragment() {
        val mapFragment = SupportMapFragment.newInstance()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.fcv_map, mapFragment)
            .commit()

        mapFragment.getMapAsync {
            lifecycleScope.launch {
                viewModel.getAllLocationMap().join()
                map = it
                setUpCluster()
                makeMarkerItem()
            }
        }
    }

    private fun setUpCluster() {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(36.0, 127.809), 7.0f
            )
        )

        clusterManager = CustomClusterManager(requireContext(), map, binding)

        setClusterClickListeners()
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setClusterClickListeners() {
        with(clusterManager) {
            setOnClusterClickListener {
                if (map.cameraPosition.zoom >= 10f) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 12f))
                    map.moveCamera(CameraUpdateFactory.newLatLng(it.position))
                } else {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 10f))
                    map.moveCamera(CameraUpdateFactory.newLatLng(it.position))
                }

                true
            }

            setOnClusterItemClickListener {
                val zoomLevel = map.cameraPosition.zoom
                if (zoomLevel == 13f) {
                    if (segmentedButtonId == R.id.btn_request) {
                        viewModel.setClickedFrom(it.snippet)

                        findNavController()
                            .navigate(R.id.action_transportFragment_to_transportListsFragment)
                    } else {
                        val action = TransportFragmentDirections
                            .actionTransportFragmentToTransportListsFragment(true)
                        findNavController().navigate(action)
                    }
                }

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

    private fun makeMarkerItem() {
        val requestList: MutableList<TransportReq> = mutableListOf()
        val volunteerList: MutableList<TransportVol> = mutableListOf()

        if (segmentedButtonId == R.id.btn_request) {
            viewModel.transportReqMap.observe(viewLifecycleOwner) { map ->
                val reqList = map.values
                reqList.forEach { if (it !in requestList) requestList.add(it) }

                itemList.clear()
                requestList.forEach {
                    itemList.add(
                        makeItem(
                            it.title,
                            it.from.substring(0, it.from.indexOf(' ')),
                            it.from.substring(it.from.indexOf(' ') + 1),
                            it.uid,
                        )
                    )
                }
                addItems()
            }
        } else {
            viewModel.transportVolMap.observe(viewLifecycleOwner) { map ->
                val volList = map.values
                volList.forEach { if (it !in volunteerList) volunteerList.add(it) }

                itemList.clear()
                volunteerList.forEach {
                    val endIndex = if (it.fromDetail.contains(',')) {
                        it.fromDetail.indexOf(',')
                    } else {
                        it.fromDetail.length
                    }

                    itemList.add(
                        makeItem(
                            it.title,
                            it.from,
                            it.fromDetail.substring(0, endIndex),
                            it.uid,
                        )
                    )
                }
                addItems()
            }
        }
    }

    private fun makeItem(title: String, county: String, district: String, uid: String): Item {
        val districtMap = viewModel.locationMap.value!![county]!!
        var lat = districtMap[district]?.latitude!!.toDouble()
        var lng = districtMap[district]?.longitude!!.toDouble()

        if (latLngSet.contains(Pair(lat, lng))) {
            lat += (Random.nextDouble() - 0.5) / 100
            lng += (Random.nextDouble() - 0.5) / 100
            latLngSet.add(Pair(lat, lng))
        } else {
            latLngSet.add(Pair(lat, lng))
        }

        return Item(title, county, district, uid, lat, lng)
    }

    private fun addItems() {
        clusterManager.clearItems()
        itemList.forEach {
            val offsetItem =
                MarkerItem(LatLng(it.lat, it.lng), it.title, "${it.county} ${it.district}")

            Log.d("as", "${it.lat} ${it.lng}")
            clusterManager.addItem(offsetItem)
        }
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
                            segmentedButtonId = R.id.btn_request
                            latLngSet.clear()
                            setMapFragment()
                        }

                        else -> {
                            efabTransportVol.visibility = View.VISIBLE
                            efabTransportReq.visibility = View.GONE
                            segmentedButtonId = R.id.btn_volunteer
                            latLngSet.clear()
                            setMapFragment()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}