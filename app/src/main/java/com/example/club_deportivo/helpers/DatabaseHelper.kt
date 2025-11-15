package com.example.club_deportivo.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "sport_club.db"

        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_FULL_NAME = "full_name"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_ROLE = "role"
        const val COLUMN_USER_CREATED_AT = "created_at"
        const val COLUMN_USER_UPDATED_AT = "updated_at"

        const val TABLE_CLIENTS = "clients"
        const val COLUMN_CLIENT_ID = "id"
        const val COLUMN_CLIENT_USER_ID = "user_id"
        const val COLUMN_CLIENT_DOCUMENT = "document"
        const val COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE = "has_valid_medical_aptitude"

        const val TABLE_MEMBERSHIPS = "memberships"
        const val COLUMN_MEMBERSHIP_ID = "id"
        const val COLUMN_MEMBERSHIP_CLIENT_ID = "client_id"
        const val COLUMN_MEMBERSHIP_TYPE = "type"
        const val COLUMN_MEMBERSHIP_AMOUNT = "amount"
        const val COLUMN_MEMBERSHIP_PAYMENT_STATUS = "payment_status"
        const val COLUMN_MEMBERSHIP_EXPIRATION_DATE = "expiration_date"

        const val TABLE_ACTIVITIES = "activities"
        const val COLUMN_ACTIVITY_ID = "id"
        const val COLUMN_ACTIVITY_NAME = "name"
        const val COLUMN_ACTIVITY_INSTRUCTOR = "instructor"
        const val COLUMN_ACTIVITY_SCHEDULE = "schedule"
        const val COLUMN_ACTIVITY_MONTHLY_PRICE = "monthly_price"
        const val COLUMN_ACTIVITY_DESCRIPTION = "description"
        const val COLUMN_ACTIVITY_MAX_CAPACITY = "max_capacity"
        const val COLUMN_ACTIVITY_IS_ACTIVE = "is_active"
        const val COLUMN_ACTIVITY_DURATION = "duration"
        const val COLUMN_ACTIVITY_LEVEL = "level"
        const val COLUMN_ACTIVITY_ROOM = "room"

        const val TABLE_ACTIVITY_ENROLLMENTS = "activity_enrollments"
        const val COLUMN_ENROLLMENT_ID = "id"
        const val COLUMN_ENROLLMENT_USER_ID = "user_id"
        const val COLUMN_ENROLLMENT_ACTIVITY_ID = "activity_id"
        const val COLUMN_ENROLLMENT_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTableSQL = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY,
                $COLUMN_USER_FULL_NAME TEXT NOT NULL,
                $COLUMN_USER_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_USER_PASSWORD TEXT NOT NULL,
                $COLUMN_USER_ROLE TEXT NOT NULL,
                $COLUMN_USER_CREATED_AT TEXT DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_USER_UPDATED_AT TEXT DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
        db.execSQL(createUsersTableSQL)

        val createClientsTableSQL = """
            CREATE TABLE $TABLE_CLIENTS (
                $COLUMN_CLIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CLIENT_USER_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_CLIENT_DOCUMENT TEXT NOT NULL UNIQUE,
                $COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_CLIENT_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()
        db.execSQL(createClientsTableSQL)

        val createMembershipsTableSQL = """
            CREATE TABLE $TABLE_MEMBERSHIPS (
                $COLUMN_MEMBERSHIP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MEMBERSHIP_CLIENT_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_MEMBERSHIP_TYPE TEXT NOT NULL,
                $COLUMN_MEMBERSHIP_AMOUNT TEXT NOT NULL,
                $COLUMN_MEMBERSHIP_PAYMENT_STATUS TEXT NOT NULL,
                $COLUMN_MEMBERSHIP_EXPIRATION_DATE TEXT,
                FOREIGN KEY($COLUMN_MEMBERSHIP_CLIENT_ID) REFERENCES $TABLE_CLIENTS($COLUMN_CLIENT_ID)
            )
        """.trimIndent()
        db.execSQL(createMembershipsTableSQL)

        val createActivitiesTableSQL = """
            CREATE TABLE $TABLE_ACTIVITIES (
                $COLUMN_ACTIVITY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ACTIVITY_NAME TEXT NOT NULL,
                $COLUMN_ACTIVITY_INSTRUCTOR TEXT,
                $COLUMN_ACTIVITY_SCHEDULE TEXT,
                $COLUMN_ACTIVITY_MONTHLY_PRICE DECIMAL NOT NULL,
                $COLUMN_ACTIVITY_DESCRIPTION TEXT,
                $COLUMN_ACTIVITY_MAX_CAPACITY INTEGER,
                $COLUMN_ACTIVITY_IS_ACTIVE INTEGER NOT NULL DEFAULT 1,
                $COLUMN_ACTIVITY_DURATION INTEGER NOT NULL,
                $COLUMN_ACTIVITY_LEVEL TEXT NOT NULL,
                $COLUMN_ACTIVITY_ROOM TEXT
            )
        """.trimIndent()
        db.execSQL(createActivitiesTableSQL)

        val createActivityEnrollmentsTableSQL = """
            CREATE TABLE $TABLE_ACTIVITY_ENROLLMENTS (
                $COLUMN_ENROLLMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ENROLLMENT_USER_ID INTEGER NOT NULL,
                $COLUMN_ENROLLMENT_ACTIVITY_ID INTEGER NOT NULL,
                $COLUMN_ENROLLMENT_STATUS TEXT NOT NULL,
                FOREIGN KEY($COLUMN_ENROLLMENT_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY($COLUMN_ENROLLMENT_ACTIVITY_ID) REFERENCES $TABLE_ACTIVITIES($COLUMN_ACTIVITY_ID)
            )
        """.trimIndent()
        db.execSQL(createActivityEnrollmentsTableSQL)

        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop tables in reverse order to respect foreign keys
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITY_ENROLLMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBERSHIPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        data class InitialUser(val id: Int, val fullName: String, val email: String, val password: String, val role: String)
        data class InitialClient(val id: Int, val userId: Int, val document: String, val hasValidMedicalAptitude: Boolean)
        data class InitialMembership(val clientId: Int, val type: String, val amount: String, val status: String, val expiration: String)
        data class InitialActivity(val name: String, val instructor: String, val schedule: String, val monthlyPrice: Int, val description: String, val maxCapacity: Int, val isActive: Boolean, val duration: Int, val level: String, val room: String)
        data class InitialEnrollment(val userId: Int, val activityId: Int, val status: String)

        val initialUsers = listOf(
            InitialUser(0, "Admin", "admin@sportclub.com", "admin123456", "ADMIN"),
            InitialUser(1, "María Gómez", "mg@gmail.com", "mg123456", "CLIENT"),
            InitialUser(2, "Carlos Ruiz", "cr@gmail.com", "cr123456", "CLIENT"),
            InitialUser(3, "Ana Torres", "at@gmail.com", "at123456", "CLIENT"),
            InitialUser(4, "Luis Fernández", "lf@gmail.com", "lf123456", "CLIENT"),
            InitialUser(5, "Elena Moreno", "em@gmail.com", "em123456", "CLIENT"),
            InitialUser(6, "David Jiménez", "dj@gmail.com", "dj123456", "CLIENT"),
            InitialUser(7, "Laura Navarro", "ln@gmail.com", "ln123456", "CLIENT"),
            InitialUser(8, "Pedro Romero", "pr@gmail.com", "pr123456", "CLIENT"),
            InitialUser(9, "Sofía Castillo", "sc@gmail.com", "sc123456", "CLIENT")
        )

        val initialClients = listOf(
            InitialClient(1, 1, "12345678", true),
            InitialClient(2, 2, "23456789", false),
            InitialClient(3, 3, "34567890", true),
            InitialClient(4, 4, "45678901", true),
            InitialClient(5, 5, "56789012", false),
            InitialClient(6, 6, "67890123", true),
            InitialClient(7, 7, "78901234", true),
            InitialClient(8, 8, "89012345", true),
            InitialClient(9, 9, "90123456", true)
        )

        val initialMemberships = listOf(
            InitialMembership(1, "NO_MEMBER", "$12000", "DUE_SOON", "2025-10-31"),
            InitialMembership(2, "MEMBER", "$35000", "OVERDUE", "2025-09-30"),
            InitialMembership(3, "NO_MEMBER", "$12000", "PAID", "2025-11-30"),
            InitialMembership(4, "MEMBER", "$15000", "DUE_SOON", "2025-10-31"),
            InitialMembership(5, "NO_MEMBER", "$24000", "OVERDUE", "2025-09-30"),
            InitialMembership(6, "MEMBER", "$15000", "PAID", "2025-11-30"),
            InitialMembership(7, "NO_MEMBER", "$12000", "DUE_SOON", "2025-10-31"),
            InitialMembership(8, "MEMBER", "$50000", "OVERDUE", "2025-09-30"),
            InitialMembership(9, "NO_MEMBER", "$12000", "PAID", "2025-11-30")
        )

        val initialActivities = listOf(
            InitialActivity("Yoga", "María García", "Lu-Mie-Vie 8:00", 15000, "Clases de yoga para todos los niveles", 20, true, 60, "BEGINNER", "Sala 1"),
            InitialActivity("Spinning", "Carlos López", "Mar-Jue 19:00", 25000, "Entrenamiento cardiovascular intenso", 15, true, 60, "INTERMEDIATE", "Sala 2"),
            InitialActivity("Pilates", "Ana Rodríguez", "Lu-Mie 18:00", 25000, "Fortalecimiento y flexibilidad", 15, true, 45, "INTERMEDIATE", "Sala 1"),
            InitialActivity("Natación", "Diego Martín", "Lu-Mie-Vie 20:00", 30000, "Clases de natación para adultos", 12, true, 60, "BEGINNER", "Pileta"),
            InitialActivity("CrossFit", "Roberto Silva", "Mar-Jue-Sáb 20:00", 20000, "Entrenamiento funcional de alta intensidad", 18, true, 60, "ADVANCED", "Sala 3"),
            InitialActivity("Meditación", "Juan Pérez", "Mar 14:00-15:30", 12000, "Técnicas de relajación y mindfulness", 25, true, 90, "BEGINNER", "Sala 1")
        )

        val initialEnrollments = listOf(
            InitialEnrollment(2, 1, "inactive"),
            InitialEnrollment(2, 2, "inactive"),
            InitialEnrollment(2, 3, "inactive"),
            InitialEnrollment(2, 4, "inactive"),
            InitialEnrollment(2, 5, "inactive"),
            InitialEnrollment(2, 6, "inactive"),
            InitialEnrollment(4, 1, "active"),
            InitialEnrollment(4, 2, "active"),
            InitialEnrollment(4, 3, "active"),
            InitialEnrollment(4, 4, "active"),
            InitialEnrollment(4, 5, "active"),
            InitialEnrollment(4, 6, "active"),
            InitialEnrollment(6, 1, "active"),
            InitialEnrollment(6, 2, "active"),
            InitialEnrollment(6, 3, "active"),
            InitialEnrollment(6, 4, "active"),
            InitialEnrollment(6, 5, "active"),
            InitialEnrollment(6, 6, "active"),
            InitialEnrollment(8, 1, "pending"),
            InitialEnrollment(8, 2, "pending"),
            InitialEnrollment(8, 3, "pending"),
            InitialEnrollment(8, 4, "pending"),
            InitialEnrollment(8, 5, "pending"),
            InitialEnrollment(8, 6, "pending")
        )

        db.execSQL("PRAGMA foreign_keys=OFF;")
        db.beginTransaction()

        try {
            for (user in initialUsers) {
                val userValues = ContentValues().apply {
                    put(COLUMN_USER_ID, user.id)
                    put(COLUMN_USER_FULL_NAME, user.fullName)
                    put(COLUMN_USER_EMAIL, user.email)
                    put(COLUMN_USER_PASSWORD, user.password)
                    put(COLUMN_USER_ROLE, user.role)
                }
                db.insert(TABLE_USERS, null, userValues)
            }

            for (client in initialClients) {
                val clientValues = ContentValues().apply {
                    put(COLUMN_CLIENT_ID, client.id)
                    put(COLUMN_CLIENT_USER_ID, client.userId)
                    put(COLUMN_CLIENT_DOCUMENT, client.document)
                    put(COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE, if (client.hasValidMedicalAptitude) 1 else 0)
                }
                db.insert(TABLE_CLIENTS, null, clientValues)
            }

            for (membership in initialMemberships) {
                val membershipValues = ContentValues().apply {
                    put(COLUMN_MEMBERSHIP_CLIENT_ID, membership.clientId)
                    put(COLUMN_MEMBERSHIP_TYPE, membership.type)
                    put(COLUMN_MEMBERSHIP_AMOUNT, membership.amount)
                    put(COLUMN_MEMBERSHIP_PAYMENT_STATUS, membership.status)
                    put(COLUMN_MEMBERSHIP_EXPIRATION_DATE, membership.expiration)
                }
                db.insert(TABLE_MEMBERSHIPS, null, membershipValues)
            }

            val activityIds = mutableMapOf<Int, Long>()
            initialActivities.forEachIndexed { index, activity ->
                val activityValues = ContentValues().apply {
                    put(COLUMN_ACTIVITY_NAME, activity.name)
                    put(COLUMN_ACTIVITY_INSTRUCTOR, activity.instructor)
                    put(COLUMN_ACTIVITY_SCHEDULE, activity.schedule)
                    put(COLUMN_ACTIVITY_MONTHLY_PRICE, activity.monthlyPrice)
                    put(COLUMN_ACTIVITY_DESCRIPTION, activity.description)
                    put(COLUMN_ACTIVITY_MAX_CAPACITY, activity.maxCapacity)
                    put(COLUMN_ACTIVITY_IS_ACTIVE, if (activity.isActive) 1 else 0)
                    put(COLUMN_ACTIVITY_DURATION, activity.duration)
                    put(COLUMN_ACTIVITY_LEVEL, activity.level)
                    put(COLUMN_ACTIVITY_ROOM, activity.room)
                }
                val activityId = db.insert(TABLE_ACTIVITIES, null, activityValues)
                activityIds[index + 1] = activityId
            }

            for (enrollment in initialEnrollments) {
                val realActivityId = activityIds[enrollment.activityId]
                if (realActivityId != null) {
                    val enrollmentValues = ContentValues().apply {
                        put(COLUMN_ENROLLMENT_USER_ID, enrollment.userId)
                        put(COLUMN_ENROLLMENT_ACTIVITY_ID, realActivityId)
                        put(COLUMN_ENROLLMENT_STATUS, enrollment.status)
                    }
                    db.insert(TABLE_ACTIVITY_ENROLLMENTS, null, enrollmentValues)
                }
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }
}
