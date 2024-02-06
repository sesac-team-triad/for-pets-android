package com.teamtriad.forpets.ui.transport.marker

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.teamtriad.forpets.databinding.FragmentTransportBinding

class CustomClusterManager(
    context: Context,
    map: GoogleMap,
    private val binding: FragmentTransportBinding
) : ClusterManager<MarkerItem>(context, map) {

    override fun onCameraIdle() {
        super.onCameraIdle()
        with(binding) {
            efabTransportReq.extend()
            efabTransportVol.extend()
        }
    }
}