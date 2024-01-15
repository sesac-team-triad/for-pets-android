package com.teamtriad.forpets.model

import com.squareup.moshi.Json

data class AbandonmentInfo(
    @Json(name = "filename") val thumbnailImageUrl: String,
    @Json(name = "happenDt") val happenDate: String,
    val happenPlace: String,
    @Json(name = "kindCd") val kind: String,
    @Json(name = "colorCd") val color: String,
    val age: String,
    val weight: String,
    val noticeNo: String,
    @Json(name = "popfile") val imageUrl: String,
    val processState: String,
    @Json(name = "sexCd") val sex: String,
    val neuterYn: String,
    val specialMark: String,                    // 특징
    val careNm: String,                         // 보호소이름
    val careTel: String,                        // 보호소전화번호
    val careAddr: String,                       // 보호장소
    val chargeNm: String,                       // 담당자
    val officetel: String,                      // 담당자연락처
    val noticeComment: String?
)