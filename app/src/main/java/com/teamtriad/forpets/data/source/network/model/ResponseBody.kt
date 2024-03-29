package com.teamtriad.forpets.data.source.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseBody(
    val items: Items,
    // 한 페이지 결과 수
    val numOfRows: Int,
    // 페이지 번호
    val pageNo: Int,
    // 전체 결과 수
    val totalCount: Int,
)

@JsonClass(generateAdapter = true)
data class Items(
    val item: List<AbandonmentInfo>?,
)