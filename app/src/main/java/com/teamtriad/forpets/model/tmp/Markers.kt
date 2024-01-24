package com.teamtriad.forpets.model.tmp

import com.google.android.gms.maps.model.LatLng

data class Markers(
    val place: LatLng,
    val title: String,
)

object Places {
    fun getMarkerData(): List<Markers> {
        return mutableListOf(
            Markers(
                LatLng(37.560, 127.064),
                title = "새싹"
            ),
            Markers(
                LatLng(37.550937, 126.849642),
                title = "강서구"
            ),
            Markers(
                LatLng(37.5247583, 129.1142625),
                title = "동해시"
            ),
            Markers(
                LatLng(37.263476, 127.028646),
                title = "수원시"
            ),
            Markers(
                LatLng(36.984539, 128.365589),
                title = "단양군"
            ),
            Markers(
                LatLng(36.3623219, 127.3562683),
                title = "유성구"
            ),
            Markers(
                LatLng(35.8581654, 128.630625),
                title = "수성구"
            ),
            Markers(
                LatLng(35.163177, 129.163634),
                title = "해운대구"
            ),
            Markers(
                LatLng(34.760425, 127.662313),
                title = "여수시"
            ),
            Markers(
                LatLng(35.1330039, 126.902402),
                title = "남구"
            ),
            Markers(
                LatLng(33.499568, 126.531138),
                title = "제주시"
            )
        )
    }
}

