package com.teamtriad.forpets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtriad.forpets.data.TransportRepository
import com.teamtriad.forpets.data.source.network.RemoteDatabaseService
import com.teamtriad.forpets.data.source.network.model.Appointment
import com.teamtriad.forpets.data.source.network.model.Location
import com.teamtriad.forpets.data.source.network.model.Moving
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.data.source.network.model.TransportVol
import kotlinx.coroutines.launch

class TransportViewModel : ViewModel() {

    private val transportRepository = TransportRepository(RemoteDatabaseService.getService())
//    private val locationRepository = LocationRepository()
//    private val appointmentRepository = AppointmentRepository()
//    private val userRepository = UserRepository()
//    private val storageRepository = StorageRepository()

    private var _transportReqMap = MutableLiveData<Map<String, TransportReq>>()
    val transportReqMap: LiveData<Map<String, TransportReq>> get() = _transportReqMap

    private var _transportVolMap = MutableLiveData<Map<String, TransportVol>>()
    val transportVolMap: LiveData<Map<String, TransportVol>> get() = _transportVolMap

    private var _locationMap = MutableLiveData<Map<String, Map<String, Location>>>()
    val locationMap: LiveData<Map<String, Map<String, Location>>> get() = _locationMap

    private var _appointmentMap = MutableLiveData<Map<String, Appointment>>()
    val appointmentMap: LiveData<Map<String, Appointment>> get() = _appointmentMap

    private var _movingMap = MutableLiveData<Map<String, Moving>>()
    val movingMap: LiveData<Map<String, Moving>> get() = _movingMap

    init {
        _transportReqMap.value = mapOf()
        _transportVolMap.value = mapOf()
        _appointmentMap.value = mapOf()
        _movingMap.value = mapOf()
        _locationMap.value = mapOf()
    }

    /**
     * 이동봉사 요청글을 저장합니다.
     */
    fun addTransportReq(transportReq: TransportReq) {
        viewModelScope.launch {
            transportRepository.addTransportReq(transportReq)
        }
    }

    /**
     * 등록된 이동봉사 요청글들의 목록을 전부 가져옵니다.
     */
    fun getAllTransportReqMap() {
        viewModelScope.launch {
            _transportReqMap.value = transportRepository.getAllTransportReqMap() ?: mapOf()
        }
    }

    /**
     * 이동봉사 요청글을 가져옵니다.
     */
    suspend fun getTransportReqByKey(key: String): TransportReq? {
        return transportRepository.getTransportReqByKey(key)
    }

//    /**
//     * 이동봉사 요청글을 변경합니다.
//     */
//    fun updateTransportReqByKey(key: String, transportReq: TransportReq) {
//        viewModelScope.launch {
//            transportRepository.updateTransportReqByKey(key, transportReq)
//        }
//    }
//
//    /**
//     * 이동봉사 요청글을 삭제합니다.
//     */
//    fun deleteTransportReqByKey(key: String) {
//        viewModelScope.launch {
//            transportRepository.deleteTransportReqByKey(key)
//        }
//    }
//
//    /**
//     * 봉사자 글을 저장합니다.
//     */
//    fun addTransportVol(transportVol: TransportVol) {
//        viewModelScope.launch {
//            transportRepository.addTransportVol(transportVol)
//        }
//    }
//
//    /**
//     * 등록된 봉사자 글들의 목록을 전부 가져옵니다.
//     */
//    fun getAllTransportVolMap() {
//        viewModelScope.launch {
//            _transportVolMap.value = transportRepository.getAllTransportVolMap()
//        }
//    }
//
//    /**
//     * 봉사자 글을 가져옵니다.
//     */
//    fun getTransportVolByKey(key: String): TransportVol {
//        return transportRepository.getTransportVolByKey(key)
//    }
//
//    /**
//     * 봉사자 글을 변경합니다.
//     */
//    fun updateTransportVolByKey(key: String, transportVol: TransportVol) {
//        viewModelScope.launch {
//            transportRepository.updateTransportVolByKey(key, transportVol)
//        }
//    }
//
//    /**
//     * 봉사자 글을 삭제합니다.
//     */
//    fun deleteTransportVolByKey(key: String) {
//        viewModelScope.launch {
//            transportRepository.deleteTransportVolByKey(key)
//        }
//    }
}