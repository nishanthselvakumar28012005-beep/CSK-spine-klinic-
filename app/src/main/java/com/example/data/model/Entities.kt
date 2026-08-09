package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val durationMinutes: Int,
    val priceMin: Int,
    val priceMax: Int,
    val category: String,
    val description: String,
    val isAvailable: Boolean = true
)

@Entity(tableName = "clinic_hours")
data class ClinicHoursEntity(
    @PrimaryKey val dayOfWeek: Int, // 1=Sunday, 2=Monday, ..., 7=Saturday
    val dayName: String,
    val openTime: String, // HH:mm format, e.g., "07:30"
    val closeTime: String, // HH:mm format, e.g., "00:30"
    val isOpen: Boolean
)

@Entity(tableName = "blocked_slots")
data class BlockedSlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g., "09:00 AM" or "ALL_DAY"
    val reason: String
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val appointmentId: String, // e.g. CSK-2026-00001
    val serviceId: Int,
    val serviceName: String,
    val serviceDuration: Int,
    val priceAmount: Double,
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g. "09:00 AM"
    val patientName: String,
    val patientMobile: String,
    val patientEmail: String,
    val patientAge: Int,
    val reason: String,
    val status: String = "pending", // pending, confirmed, cancelled, completed, no-show
    val paymentStatus: String = "payment_pending", // payment_pending, payment_paid, payment_failed, payment_refunded
    val paymentMethod: String = "Pay at Clinic",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val readTimeMinutes: Int,
    val summary: String,
    val content: String,
    val datePublished: String
)
