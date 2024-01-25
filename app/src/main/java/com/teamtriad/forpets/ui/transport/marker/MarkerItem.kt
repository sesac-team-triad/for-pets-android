package com.teamtriad.forpets.ui.transport.marker

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerItem(
    place: LatLng,
    title: String,
    snippet: String
) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String

    override fun getPosition(): LatLng = position

    override fun getTitle(): String = title

    override fun getSnippet(): String = snippet

    override fun getZIndex(): Float = 0f

    init {
        position = place
        this.title = title
        this.snippet = snippet
    }
}