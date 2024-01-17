package com.teamtriad.forpets.model

data class ResponseHeader(
    // 요청 고유번호
    val reqNo: Int?,
    // 결과코드
    val resultCode: String,
    val resultMsg: String,
    val errorMsg: String?,
)