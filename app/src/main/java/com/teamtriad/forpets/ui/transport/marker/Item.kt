package com.teamtriad.forpets.ui.transport.marker

import com.google.android.gms.maps.model.LatLng

data class Item(
    val title: String,
    val county: String,
    val district: String,
    val uid: String,
    val place: LatLng,
)
