package com.teamtriad.forpets.viewmodel

import androidx.lifecycle.ViewModel
import com.teamtriad.forpets.ForPetsApplication.Companion.adoptService
import com.teamtriad.forpets.data.AdoptRepository
import com.teamtriad.forpets.data.source.network.model.AbandonmentInfo

class AdoptViewModel : ViewModel() {

    private val adoptRepository = AdoptRepository(adoptService)

    private var _abandonmentInfoList = listOf<AbandonmentInfo>()
    val abandonmentInfoList: List<AbandonmentInfo> get() = _abandonmentInfoList

    private var pageNo: Int = 0         // 마지막으로 요청한 페이지 번호(pageNo) 매개변수의 값을 관리

    suspend fun getAbandonmentInfos(): List<AbandonmentInfo>? {
        val abandonmentInfos = adoptRepository.getAbandonmentInfos(++pageNo)

        if (abandonmentInfos == null) pageNo--

        return abandonmentInfos
    }

    fun setAbandonmentInfoList(abandonmentInfos: List<AbandonmentInfo>) {
        _abandonmentInfoList = abandonmentInfos
    }
}