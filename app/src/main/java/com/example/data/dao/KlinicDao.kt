package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface KlinicDao {

    // --- Services ---
    @Query("SELECT * FROM services ORDER BY id ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Update
    suspend fun updateService(service: ServiceEntity)

    // --- Clinic Hours ---
    @Query("SELECT * FROM clinic_hours ORDER BY dayOfWeek ASC")
    fun getClinicHours(): Flow<List<ClinicHoursEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClinicHours(hours: List<ClinicHoursEntity>)

    @Update
    suspend fun updateClinicHours(hours: ClinicHoursEntity)

    // --- Blocked Slots ---
    @Query("SELECT * FROM blocked_slots ORDER BY date ASC")
    fun getBlockedSlots(): Flow<List<BlockedSlotEntity>>

    @Query("SELECT * FROM blocked_slots WHERE date = :date")
    suspend fun getBlockedSlotsForDate(date: String): List<BlockedSlotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedSlot(slot: BlockedSlotEntity)

    @Delete
    suspend fun deleteBlockedSlot(slot: BlockedSlotEntity)

    // --- Appointments ---
    @Query("SELECT * FROM appointments ORDER BY createdAt DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE appointmentId = :id")
    suspend fun getAppointmentById(id: String): AppointmentEntity?

    @Query("SELECT * FROM appointments WHERE date = :date AND status != 'cancelled'")
    suspend fun getActiveAppointmentsForDate(date: String): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Query("UPDATE appointments SET status = :status WHERE appointmentId = :id")
    suspend fun updateAppointmentStatus(id: String, status: String)

    @Query("UPDATE appointments SET date = :newDate, timeSlot = :newSlot WHERE appointmentId = :id")
    suspend fun rescheduleAppointment(id: String, newDate: String, newSlot: String)

    @Query("SELECT COUNT(*) FROM appointments WHERE date = :date AND timeSlot = :slot AND status != 'cancelled'")
    suspend fun countAppointmentsForSlot(date: String, slot: String): Int

    // --- Articles / Knowledge Base ---
    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)
}
