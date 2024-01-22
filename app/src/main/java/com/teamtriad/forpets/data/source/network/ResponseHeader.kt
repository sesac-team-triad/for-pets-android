package com.teamtriad.forpets.data.source.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseHeader(
    // 요청 고유번호
    val reqNo: Int?,
    // 결과코드
    val resultCode: String,
    val resultMsg: String,
    val errorMsg: String?,
)