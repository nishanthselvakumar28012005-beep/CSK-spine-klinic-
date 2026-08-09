package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.KlinicDao
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ServiceEntity::class,
        ClinicHoursEntity::class,
        BlockedSlotEntity::class,
        AppointmentEntity::class,
        ArticleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KlinicDatabase : RoomDatabase() {

    abstract fun klinicDao(): KlinicDao

    companion object {
        @Volatile
        private var INSTANCE: KlinicDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): KlinicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KlinicDatabase::class.java,
                    "charlies_spine_klinic_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.klinicDao())
                }
            }
        }

        suspend fun populateInitialData(dao: KlinicDao) {
            // Initial Services
            val services = listOf(
                ServiceEntity(
                    id = 1,
                    name = "Spine & Joint Assessment (15 Min)",
                    durationMinutes = 15,
                    priceMin = 1000,
                    priceMax = 1000,
                    category = "Assessment",
                    description = "Rapid diagnostic examination of cervical, thoracic & lumbar spine posture and nerve compression."
                ),
                ServiceEntity(
                    id = 2,
                    name = "Comprehensive Spine Assessment (30 Min)",
                    durationMinutes = 30,
                    priceMin = 1500,
                    priceMax = 1500,
                    category = "Assessment",
                    description = "In-depth biomechanical assessment, gait analysis, reflex testing and tailored rehabilitation roadmap."
                ),
                ServiceEntity(
                    id = 3,
                    name = "Specialist Manual Therapy",
                    durationMinutes = 30,
                    priceMin = 1500,
                    priceMax = 2500,
                    category = "Manual Therapy",
                    description = "Hands-on spinal mobilization, myofascial release, joint manipulation & CMT technique by Charles P. Joseph."
                ),
                ServiceEntity(
                    id = 4,
                    name = "Online Tele-Physio Consultation",
                    durationMinutes = 20,
                    priceMin = 1000,
                    priceMax = 1000,
                    category = "Online",
                    description = "Virtual video consultation, posture analysis & guided home exercise protocol for remote patients."
                ),
                ServiceEntity(
                    id = 5,
                    name = "Home Physiotherapy Visit",
                    durationMinutes = 45,
                    priceMin = 650,
                    priceMax = 750,
                    category = "Home Care",
                    description = "Specialized doorstep physiotherapy care for acute pain, post-op spine rehabilitation & geriatric patients."
                )
            )
            dao.insertServices(services)

            // Initial Clinic Hours (1=Sun, 2=Mon ... 7=Sat)
            val clinicHours = listOf(
                ClinicHoursEntity(1, "Sunday", "07:30", "00:30", isOpen = false),
                ClinicHoursEntity(2, "Monday", "07:30", "00:30", isOpen = true),
                ClinicHoursEntity(3, "Tuesday", "07:30", "00:30", isOpen = true),
                ClinicHoursEntity(4, "Wednesday", "07:30", "00:30", isOpen = true),
                ClinicHoursEntity(5, "Thursday", "07:30", "00:30", isOpen = true),
                ClinicHoursEntity(6, "Friday", "07:30", "00:30", isOpen = true),
                ClinicHoursEntity(7, "Saturday", "07:30", "00:30", isOpen = false)
            )
            dao.insertClinicHours(clinicHours)

            // Initial Knowledge Base Articles
            val articles = listOf(
                ArticleEntity(
                    id = 1,
                    title = "Understanding Sciatica & Disc Bulge Management",
                    category = "Spine Health",
                    readTimeMinutes = 4,
                    summary = "Learn how targeted manual therapy and decompression exercises relieve nerve impingement without immediate surgery.",
                    content = "Sciatica occurs when the sciatic nerve running down your spine through your hip and leg gets compressed. Common triggers include herniated discs and bone spurs. Early clinical intervention using CMT (Certified Manual Therapy) helps decompress spinal nerves, restore mobility, and prevent chronic lumbar stiffness.",
                    datePublished = "2026-08-01"
                ),
                ArticleEntity(
                    id = 2,
                    title = "5 Ergonomic Home Exercises for Neck & Upper Back Pain",
                    category = "Ergonomics",
                    readTimeMinutes = 3,
                    summary = "Simple postural correction techniques for desk workers to prevent forward-head posture and trapezial muscle spasms.",
                    content = "Modern screen time leads to text neck syndrome, placing up to 27kg of strain on the cervical spine. Practicing chin tucks, scapular retractions, and thoracic extension stretches twice daily relieves pressure and preserves normal spinal curvature.",
                    datePublished = "2026-08-04"
                ),
                ArticleEntity(
                    id = 3,
                    title = "Role of Manual Therapy in Sports & Spinal Injury Rehabilitation",
                    category = "Physiotherapy",
                    readTimeMinutes = 5,
                    summary = "Why specialized hands-on joint mobilization accelerates recovery faster than passive thermal modalities alone.",
                    content = "Manual therapy incorporates targeted joint manipulation, soft tissue mobilization, and neurodynamic sliding. Used extensively by sports medicine specialists, it improves localized blood circulation, reduces muscle guarding, and resets neuromuscular firing patterns.",
                    datePublished = "2026-08-07"
                )
            )
            dao.insertArticles(articles)
        }
    }
}
