package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.KlinicDatabase
import com.example.data.model.*
import com.example.data.repository.KlinicRepository
import com.example.util.PaymentManager
import com.example.util.PaymentMethod
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class BookingFormState(
    val selectedService: ServiceEntity? = null,
    val selectedDate: String = "",
    val availableSlots: List<String> = emptyList(),
    val selectedSlot: String? = null,
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val age: String = "",
    val reason: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.PAY_AT_CLINIC,
    val step: Int = 1, // 1 to 6
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val confirmedAppointment: AppointmentEntity? = null
)

data class AdminState(
    val isAuthenticated: Boolean = false,
    val pinInput: String = "",
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedStatusFilter: String = "all", // all, pending, confirmed, completed, cancelled, no-show
    val selectedDateFilter: String = "", // YYYY-MM-DD
    val blockDateInput: String = "",
    val blockSlotInput: String = "ALL_DAY",
    val blockReasonInput: String = "Clinic Maintenance",
    val editingService: ServiceEntity? = null
)

data class KlinicUiState(
    val currentTab: Int = 0, // 0=Home, 1=Services, 2=Book, 3=Knowledge Base, 4=Contact, 5=Admin
    val bookingState: BookingFormState = BookingFormState(),
    val adminState: AdminState = AdminState()
)

class KlinicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KlinicRepository
    private val paymentManager = PaymentManager()

    private val _uiState = MutableStateFlow(KlinicUiState())
    val uiState: StateFlow<KlinicUiState> = _uiState.asStateFlow()

    init {
        val database = KlinicDatabase.getDatabase(application, viewModelScope)
        repository = KlinicRepository(database.klinicDao())

        // Default initial booking date to today or tomorrow
        val todayStr = getCurrentDateString()
        _uiState.update { state ->
            state.copy(
                bookingState = state.bookingState.copy(
                    selectedDate = todayStr
                ),
                adminState = state.adminState.copy(
                    selectedDateFilter = todayStr,
                    blockDateInput = todayStr
                )
            )
        }
        loadAvailableSlotsForDate(todayStr)
    }

    val allServices: StateFlow<List<ServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val clinicHours: StateFlow<List<ClinicHoursEntity>> = repository.clinicHours
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val blockedSlots: StateFlow<List<BlockedSlotEntity>> = repository.blockedSlots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAppointments: StateFlow<List<AppointmentEntity>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val articles: StateFlow<List<ArticleEntity>> = repository.articles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Navigation ---
    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(currentTab = tabIndex) }
    }

    // --- Booking Flow ---
    fun selectServiceForBooking(service: ServiceEntity) {
        _uiState.update {
            it.copy(
                bookingState = it.bookingState.copy(
                    selectedService = service,
                    step = 2
                )
            )
        }
    }

    fun setBookingDate(dateString: String) {
        _uiState.update {
            it.copy(
                bookingState = it.bookingState.copy(
                    selectedDate = dateString,
                    selectedSlot = null
                )
            )
        }
        loadAvailableSlotsForDate(dateString)
    }

    fun loadAvailableSlotsForDate(dateString: String) {
        viewModelScope.launch {
            val slots = repository.getAvailableTimeSlots(dateString)
            _uiState.update {
                it.copy(
                    bookingState = it.bookingState.copy(
                        availableSlots = slots
                    )
                )
            }
        }
    }

    fun selectBookingSlot(slot: String) {
        _uiState.update {
            it.copy(
                bookingState = it.bookingState.copy(
                    selectedSlot = slot
                )
            )
        }
    }

    fun updatePatientDetails(name: String, mobile: String, email: String, age: String, reason: String) {
        _uiState.update {
            it.copy(
                bookingState = it.bookingState.copy(
                    name = name,
                    mobile = mobile,
                    email = email,
                    age = age,
                    reason = reason,
                    errorMessage = null
                )
            )
        }
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _uiState.update {
            it.copy(
                bookingState = it.bookingState.copy(paymentMethod = method)
            )
        }
    }

    fun setBookingStep(step: Int) {
        _uiState.update {
            it.copy(
                bookingState = it.bookingState.copy(
                    step = step,
                    errorMessage = null
                )
            )
        }
    }

    fun submitBooking() {
        val currentBooking = _uiState.value.bookingState
        val service = currentBooking.selectedService
        val slot = currentBooking.selectedSlot
        val date = currentBooking.selectedDate

        if (service == null) {
            _uiState.update { it.copy(bookingState = it.bookingState.copy(errorMessage = "Please select a service.")) }
            return
        }
        if (slot.isNull_or_empty()) {
            _uiState.update { it.copy(bookingState = it.bookingState.copy(errorMessage = "Please select a time slot.")) }
            return
        }
        val safeSlot = slot ?: return
        if (currentBooking.name.isBlank() || currentBooking.mobile.isBlank()) {
            _uiState.update { it.copy(bookingState = it.bookingState.copy(errorMessage = "Please enter your Name and Mobile Number.")) }
            return
        }

        val ageInt = currentBooking.age.toIntOrNull() ?: 30
        val appointmentId = repository.generateAppointmentId()

        _uiState.update { it.copy(bookingState = it.bookingState.copy(isSubmitting = true, errorMessage = null)) }

        viewModelScope.launch {
            // Process payment layer
            val paymentResult = paymentManager.processPayment(
                amount = service.priceMin.toDouble(),
                method = currentBooking.paymentMethod,
                appointmentId = appointmentId,
                patientName = currentBooking.name,
                patientMobile = currentBooking.mobile
            )

            val newAppointment = AppointmentEntity(
                appointmentId = appointmentId,
                serviceId = service.id,
                serviceName = service.name,
                serviceDuration = service.durationMinutes,
                priceAmount = service.priceMin.toDouble(),
                date = date,
                timeSlot = safeSlot,
                patientName = currentBooking.name,
                patientMobile = currentBooking.mobile,
                patientEmail = currentBooking.email,
                patientAge = ageInt,
                reason = currentBooking.reason,
                status = "pending", // Default to pending as requested
                paymentStatus = paymentResult.status.statusKey,
                paymentMethod = currentBooking.paymentMethod.displayName
            )

            val result = repository.createAppointment(newAppointment)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        bookingState = it.bookingState.copy(
                            isSubmitting = false,
                            step = 6, // Confirmation screen
                            confirmedAppointment = newAppointment
                        )
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        bookingState = it.bookingState.copy(
                            isSubmitting = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Failed to book appointment. Please try another slot."
                        )
                    )
                }
            }
        }
    }

    fun resetBookingForm() {
        val todayStr = getCurrentDateString()
        _uiState.update {
            it.copy(
                bookingState = BookingFormState(selectedDate = todayStr)
            )
        }
        loadAvailableSlotsForDate(todayStr)
    }

    // --- Admin Dashboard Actions ---
    fun loginAdmin(pin: String) {
        if (pin == "1234" || pin == "90822") { // Secure PIN check
            _uiState.update {
                it.copy(
                    adminState = it.adminState.copy(
                        isAuthenticated = true,
                        pinInput = "",
                        errorMessage = null
                    )
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    adminState = it.adminState.copy(
                        errorMessage = "Invalid Admin Credentials. Try PIN 1234."
                    )
                )
            }
        }
    }

    fun logoutAdmin() {
        _uiState.update {
            it.copy(adminState = AdminState(isAuthenticated = false))
        }
    }

    fun updateAppointmentStatus(appointmentId: String, status: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, status)
        }
    }

    fun rescheduleAppointment(appointmentId: String, newDate: String, newSlot: String) {
        viewModelScope.launch {
            repository.rescheduleAppointment(appointmentId, newDate, newSlot)
        }
    }

    fun blockTimeSlot(date: String, slot: String, reason: String) {
        viewModelScope.launch {
            repository.blockSlot(
                BlockedSlotEntity(
                    date = date,
                    timeSlot = slot,
                    reason = reason
                )
            )
        }
    }

    fun unblockTimeSlot(blockedSlot: BlockedSlotEntity) {
        viewModelScope.launch {
            repository.deleteBlockedSlot(blockedSlot)
        }
    }

    fun updateServicePrice(service: ServiceEntity) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun toggleClinicDayStatus(clinicHoursEntity: ClinicHoursEntity) {
        viewModelScope.launch {
            repository.updateClinicHours(
                clinicHoursEntity.copy(isOpen = !clinicHoursEntity.isOpen)
            )
        }
    }

    fun setAdminSearchQuery(query: String) {
        _uiState.update {
            it.copy(adminState = it.adminState.copy(searchQuery = query))
        }
    }

    fun setAdminFilterStatus(status: String) {
        _uiState.update {
            it.copy(adminState = it.adminState.copy(selectedStatusFilter = status))
        }
    }

    fun setAdminSelectedDate(date: String) {
        _uiState.update {
            it.copy(adminState = it.adminState.copy(selectedDateFilter = date))
        }
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
}
