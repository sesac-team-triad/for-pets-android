package com.teamtriad.forpets.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtriad.forpets.ForPetsApplication.Companion.remoteDatabaseService
import com.teamtriad.forpets.data.AppointmentRepository
import com.teamtriad.forpets.data.LocationRepository
import com.teamtriad.forpets.data.TransportRepository
import com.teamtriad.forpets.data.UserRepository
import com.teamtriad.forpets.data.source.network.model.Appointment
import com.teamtriad.forpets.data.source.network.model.CompletedDate
import com.teamtriad.forpets.data.source.network.model.District
import com.teamtriad.forpets.data.source.network.model.Moving
import com.teamtriad.forpets.data.source.network.model.TransportReq
import com.teamtriad.forpets.data.source.network.model.TransportVol
import com.teamtriad.forpets.ui.chat.enums.AppointmentProgress
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TransportViewModel : ViewModel() {

    private val transportRepository = TransportRepository(remoteDatabaseService)
    private val locationRepository = LocationRepository(remoteDatabaseService)
    private val appointmentRepository = AppointmentRepository(remoteDatabaseService)
    private val userRepository = UserRepository(remoteDatabaseService)

    private var _transportReqMap = MutableLiveData<Map<String, TransportReq>>()
    val transportReqMap: LiveData<Map<String, TransportReq>> get() = _transportReqMap

    private var _transportVolMap = MutableLiveData<Map<String, TransportVol>>()
    val transportVolMap: LiveData<Map<String, TransportVol>> get() = _transportVolMap

    private var _locationMap = MutableLiveData<Map<String, Map<String, District>>>()
    val locationMap: LiveData<Map<String, Map<String, District>>> get() = _locationMap

    private var _appointmentMap = MutableLiveData<Map<String, Appointment>>()
    val appointmentMap: LiveData<Map<String, Appointment>> get() = _appointmentMap

    private var _movingMap = MutableLiveData<Map<String, Moving>>()
    val movingMap: LiveData<Map<String, Moving>> get() = _movingMap

    private var _clickedFrom: String? = null
    val clickedFrom: String? get() = _clickedFrom

    private var _reqAnimalChoice = listOf<String>()
    val reqAnimalChoice: List<String> get() = _reqAnimalChoice

    private var _selectedFromCounty = MutableLiveData<String>()
    val selectedFromCounty: LiveData<String> get() = _selectedFromCounty

    private var _selectedFromDistrict = MutableLiveData<String>()
    val selectedFromDistrict: LiveData<String> get() = _selectedFromDistrict

    private var _selectedFromDistrictList = MutableLiveData<List<String>>()
    val selectedFromDistrictList: LiveData<List<String>> get() = _selectedFromDistrictList

    private var _selectedToCounty = MutableLiveData<String>()
    val selectedToCounty: LiveData<String> get() = _selectedToCounty

    private var _selectedToDistrict = MutableLiveData<String>()
    val selectedToDistrict: LiveData<String> get() = _selectedToDistrict

    private var _selectedToDistrictList = MutableLiveData<List<String>>()
    val selectedToDistrictList: LiveData<List<String>> get() = _selectedToDistrictList

    init {
        _transportReqMap.value = mapOf()
        _transportVolMap.value = mapOf()
        _locationMap.value = mapOf()
        _appointmentMap.value = mapOf()
        _movingMap.value = mapOf()
        _selectedFromCounty.value = ""
        _selectedFromDistrict.value = ""
        _selectedFromDistrictList.value = listOf()
        _selectedToCounty.value = ""
        _selectedToDistrict.value = ""
        _selectedToDistrictList.value = listOf()
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
     * 등록된 이동봉사 요청글들의 목록을 전부 가져옵니다.(LiveData)
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

    /**
     * 이동봉사 요청글을 변경합니다.
     */
    fun updateTransportReqByKey(key: String, transportReq: TransportReq) {
        viewModelScope.launch {
            transportRepository.updateTransportReqByKey(key, transportReq)
        }
    }

    /**
     * 이동봉사 요청글을 삭제합니다.
     */
    fun deleteTransportReqByKey(key: String) {
        viewModelScope.launch {
            transportRepository.deleteTransportReqByKey(key)
        }
    }

    /**
     * 봉사자 글을 저장합니다.
     */
    fun addTransportVol(transportVol: TransportVol) {
        viewModelScope.launch {
            transportRepository.addTransportVol(transportVol)
        }
    }

    /**
     * 등록된 봉사자 글들의 목록을 전부 가져옵니다.(LiveData)
     */
    fun getAllTransportVolMap() {
        viewModelScope.launch {
            _transportVolMap.value = transportRepository.getAllTransportVolMap()
        }
    }

    /**
     * 봉사자 글을 변경합니다.
     */
    fun updateTransportVolByKey(key: String, transportVol: TransportVol) {
        viewModelScope.launch {
            transportRepository.updateTransportVolByKey(key, transportVol)
        }
    }

    /**
     * 봉사자 글을 삭제합니다.
     */
    fun deleteTransportVolByKey(key: String) {
        viewModelScope.launch {
            transportRepository.deleteTransportVolByKey(key)
        }
    }

    /**
     * 등록된 시/도들의 목록을 전부 가져옵니다.(LiveData)
     */
    fun getAllLocationMap(): Job = viewModelScope.launch {
        _locationMap.value = locationRepository.getAllCountyMap() ?: mapOf()
    }

    /**
     * 약속을 저장합니다.
     */
    suspend fun addAppointment(appointment: Appointment): String? {
        return appointmentRepository.addAppointment(appointment)
    }

    /**
     * 등록된 약속들의 목록을 전부 가져옵니다.(LiveData)
     */
    fun getAllAppointmentMap() {
        viewModelScope.launch {
            _appointmentMap.value = appointmentRepository.getAllAppointmentMap() ?: mapOf()
        }
    }

    /**
     * 약속을 가져옵니다.
     */
    suspend fun getAppointmentByKey(key: String): Appointment? {
        return appointmentRepository.getAppointmentByKey(key)
    }

    /**
     * 약속의 진행 상태를 변경합니다.
     */
    fun updateAppointmentProgressByKey(key: String, progress: AppointmentProgress) {
        viewModelScope.launch {
            appointmentRepository.updateAppointmentProgressByKey(key, progress.progress)
        }
    }

    /**
     * 약속의 완료 날짜를 변경합니다.
     */
    fun updateAppointmentCompletedDateByKey(key: String, completedDate: CompletedDate) {
        viewModelScope.launch {
            appointmentRepository.updateAppointmentCompletedDateByKey(key, completedDate)
        }
    }

    /**
     * 약속을 삭제합니다.
     */
    fun deleteAppointmentByKey(key: String) {
        viewModelScope.launch {
            appointmentRepository.deleteAppointmentByKey(key)
        }
    }

    /**
     * 이동중을 저장합니다.
     */
    fun addMoving(moving: Moving) {
        viewModelScope.launch {
            appointmentRepository.addMoving(moving)
        }
    }

    /**
     * 등록된 이동중들의 목록을 전부 가져옵니다.(LiveData)
     */
    fun getAllMovingMap() = viewModelScope.launch {
        _movingMap.value = appointmentRepository.getAllMovingMap()
    }

    /**
     * 이동중을 삭제합니다.
     */
    fun deleteMovingByKey(key: String) {
        viewModelScope.launch {
            appointmentRepository.deleteMovingByKey(key)
        }
    }

    /**
     * 유저의 닉네임을 가져옵니다.
     */
    suspend fun getUserNicknameByUid(uid: String): String? {
        return userRepository.getUserNicknameByUid(uid)
    }

    fun setClickedFrom(from: String?) {
        _clickedFrom = from
    }

    fun setReqAnimalChoice(reqAnimalChoice: List<String>) {
        _reqAnimalChoice = reqAnimalChoice
    }

    fun setSelectedFromCounty(county: String) {
        _selectedFromCounty.value = county
    }

    fun setSelectedFromDistrict(district: String) {
        _selectedFromDistrict.value = district
    }

    fun setSelectedFromDistrictList(districtList: List<String>) {
        _selectedFromDistrictList.value = districtList
    }

    fun setSelectedToCounty(county: String) {
        _selectedToCounty.value = county
    }

    fun setSelectedToDistrict(district: String) {
        _selectedToDistrict.value = district
    }

    fun setSelectedToDistrictList(districtList: List<String>) {
        _selectedToDistrictList.value = districtList
    }

    fun clearAllSelectedLocations() {
        _selectedFromCounty.value = ""
        _selectedFromDistrict.value = ""
        _selectedFromDistrictList.value = listOf()
        _selectedToCounty.value = ""
        _selectedToDistrict.value = ""
        _selectedToDistrictList.value = listOf()
    }

    fun filterTransportReqMapToList(
        startDate: String,
        endDate: String,
        animal: String,
        from: String,
        to: String
    ): List<TransportReq> {
        fun compareWithDates(s: String, e: String) = startDate.isEmpty() ||
                s <= endDate && startDate <= e
        fun compareWithAnimal(s: String) = animal.isEmpty() || s == animal ||
                animal == reqAnimalChoice.last() && s !in reqAnimalChoice.subList(0, reqAnimalChoice.lastIndex)
        fun compareWithFrom(s: String) = from.isEmpty() || s == from
        fun compareWithTo(s: String) = to.isEmpty() || s == to

        return mutableListOf<TransportReq>().apply {
            transportReqMap.value?.onEachIndexed { index, entry ->
                if (compareWithDates(entry.value.startDate, entry.value.endDate) &&
                    compareWithAnimal(entry.value.animal) &&
                    compareWithFrom(entry.value.from) && compareWithTo(entry.value.to)
                ) add(
                    entry.value.apply {
                        reqIndex = index
                    }
                )
            }
        }
    }
}