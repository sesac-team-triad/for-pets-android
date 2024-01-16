package com.teamtriad.forpets.data.source.network

/**
 * 농림축산식품부 농림축산검역본부_동물보호관리시스템 유기동물 정보 조회 서비스의 활용가이드
 * 문서의 Call Back URL(.../abandonmentPublic)로 요청한 결과 메시지를 decode하는 클래스이다.
 */
data class AbandonmentPublic(
    val response: Response,
)

data class Response(
    val header: ResponseHeader,
    val body: ResponseBody,
)