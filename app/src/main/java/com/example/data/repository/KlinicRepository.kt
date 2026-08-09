package com.example.data.repository

import com.example.data.dao.KlinicDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class KlinicRepository(private val dao: KlinicDao) {

    val allServices: Flow<List<ServiceEntity>> = dao.getAllServices()
    val clinicHours: Flow<List<ClinicHoursEntity>> = dao.getClinicHours()
    val blockedSlots: Flow<List<BlockedSlotEntity>> = dao.getBlockedSlots()
    val allAppointments: Flow<List<AppointmentEntity>> = dao.getAllAppointments()
    val articles: Flow<List<ArticleEntity>> = dao.getAllArticles()

    suspend fun getAppointmentById(id: String): AppointmentEntity? {
        return dao.getAppointmentById(id)
    }

    suspend fun updateAppointmentStatus(id: String, status: String) {
        dao.updateAppointmentStatus(id, status)
    }

    suspend fun rescheduleAppointment(id: String, newDate: String, newSlot: String) {
        dao.rescheduleAppointment(id, newDate, newSlot)
    }

    suspend fun updateService(service: ServiceEntity) {
        dao.updateService(service)
    }

    suspend fun updateClinicHours(hours: ClinicHoursEntity) {
        dao.updateClinicHours(hours)
    }

    suspend fun blockSlot(slot: BlockedSlotEntity) {
        dao.insertBlockedSlot(slot)
    }

    suspend fun deleteBlockedSlot(slot: BlockedSlotEntity) {
        dao.deleteBlockedSlot(slot)
    }

    suspend fun createAppointment(appointment: AppointmentEntity): Result<String> {
        // Double-booking check
        val existingCount = dao.countAppointmentsForSlot(appointment.date, appointment.timeSlot)
        if (existingCount > 0) {
            return Result.failure(Exception("Slot ${appointment.timeSlot} on ${appointment.date} is already booked."))
        }

        val blockedForDate = dao.getBlockedSlotsForDate(appointment.date)
        if (blockedForDate.any { it.timeSlot == appointment.timeSlot || it.timeSlot == "ALL_DAY" }) {
            return Result.failure(Exception("Slot ${appointment.timeSlot} on ${appointment.date} is blocked by the clinic."))
        }

        return try {
            dao.insertAppointment(appointment)
            Result.success(appointment.appointmentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableTimeSlots(dateString: String): List<String> {
        // Date format: YYYY-MM-DD
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = try { sdf.parse(dateString) } catch (e: Exception) { null } ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1=Sun, 2=Mon ... 7=Sat

        // Standard time slots generated for clinic hours (7:30 AM to 12:30 AM)
        val defaultSlots = listOf(
            "07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM", "10:00 AM",
            "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "01:00 PM",
            "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM",
            "04:30 PM", "05:00 PM", "05:30 PM", "06:00 PM", "06:30 PM", "07:00 PM",
            "07:30 PM", "08:00 PM", "08:30 PM", "09:00 PM", "09:30 PM", "10:00 PM",
            "10:30 PM", "11:00 PM", "11:30 PM", "12:00 AM"
        )

        val activeAppointments = dao.getActiveAppointmentsForDate(dateString)
        val bookedTimeSlots = activeAppointments.map { it.timeSlot }.toSet()

        val blockedSlots = dao.getBlockedSlotsForDate(dateString)
        val blockedTimeSlots = blockedSlots.map { it.timeSlot }.toSet()

        if (blockedTimeSlots.contains("ALL_DAY")) {
            return emptyList()
        }

        return defaultSlots.filter { slot ->
            !bookedTimeSlots.contains(slot) && !blockedTimeSlots.contains(slot)
        }
    }

    fun generateAppointmentId(): String {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val randomNum = (10000..99999).random()
        return "CSK-$year-$randomNum"
    }
}
