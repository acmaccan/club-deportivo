package com.example.club_deportivo.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
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
                $COLUMN_CLIENT_ID INTEGER PRIMARY KEY,
                $COLUMN_CLIENT_USER_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_CLIENT_DOCUMENT TEXT NOT NULL UNIQUE,
                $COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_CLIENT_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()
        db.execSQL(createClientsTableSQL)

        val createMembershipsTableSQL = """
            CREATE TABLE $TABLE_MEMBERSHIPS (
                $COLUMN_MEMBERSHIP_ID INTEGER PRIMARY KEY,
                $COLUMN_MEMBERSHIP_CLIENT_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_MEMBERSHIP_TYPE TEXT NOT NULL,
                $COLUMN_MEMBERSHIP_AMOUNT TEXT NOT NULL,
                $COLUMN_MEMBERSHIP_PAYMENT_STATUS TEXT NOT NULL,
                $COLUMN_MEMBERSHIP_EXPIRATION_DATE TEXT,
                FOREIGN KEY($COLUMN_MEMBERSHIP_CLIENT_ID) REFERENCES $TABLE_CLIENTS($COLUMN_CLIENT_ID)
            )
        """.trimIndent()
        db.execSQL(createMembershipsTableSQL)

        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMBERSHIPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        data class InitialUser(val id: Int, val fullName: String, val email: String, val password: String, val role: String)
        data class InitialClient(val id: Int, val userId: Int, val document: String, val hasValidMedicalAptitude: Boolean)
        data class InitialMembership(val clientId: Int, val type: String, val amount: String, val status: String, val expiration: String)

        val initialUsers = listOf(
            InitialUser(1, "Admin", "admin@sportclub.com", "admin123456", "ADMIN"),
            InitialUser(2, "María Gómez", "mg@gmail.com", "mg123456", "CLIENT"),
            InitialUser(3, "Carlos Ruiz", "cr@gmail.com", "cr123456", "CLIENT"),
            InitialUser(4, "Ana Torres", "at@gmail.com", "at123456", "CLIENT"),
            InitialUser(5, "Luis Fernández", "lf@gmail.com", "lf123456", "CLIENT"),
            InitialUser(6, "Elena Moreno", "em@gmail.com", "em123456", "CLIENT"),
            InitialUser(7, "David Jiménez", "dj@gmail.com", "dj123456", "CLIENT"),
            InitialUser(8, "Laura Navarro", "ln@gmail.com", "ln123456", "CLIENT"),
            InitialUser(9, "Pedro Romero", "pr@gmail.com", "pr123456", "CLIENT"),
            InitialUser(10, "Sofía Castillo", "sc@gmail.com", "sc123456", "CLIENT")
        )

        val initialClients = listOf(
            InitialClient(2, 2, "12345678", true),
            InitialClient(3, 3, "23456789", false),
            InitialClient(4, 4, "34567890", true),
            InitialClient(5, 5, "45678901", true),
            InitialClient(6, 6, "56789012", false),
            InitialClient(7, 7, "67890123", true),
            InitialClient(8, 8, "78901234", true),
            InitialClient(9, 9, "89012345", true),
            InitialClient(10, 10, "90123456", true)
        )

        val initialMemberships = listOf(
            InitialMembership(2, "NO_MEMBER", "$12000", "DUE_SOON", "2025-10-31"),
            InitialMembership(3, "MEMBER", "$35000", "OVERDUE", "2025-09-30"),
            InitialMembership(4, "NO_MEMBER", "$12000", "PAID", "2025-11-30"),
            InitialMembership(5, "MEMBER", "$15000", "DUE_SOON", "2025-10-31"),
            InitialMembership(6, "NO_MEMBER", "$24000", "OVERDUE", "2025-09-30"),
            InitialMembership(7, "MEMBER", "$15000", "PAID", "2025-11-30"),
            InitialMembership(8, "NO_MEMBER", "$12000", "DUE_SOON", "2025-10-31"),
            InitialMembership(9, "MEMBER", "$50000", "OVERDUE", "2025-09-30"),
            InitialMembership(10, "NO_MEMBER", "$12000", "PAID", "2025-11-30")
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

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }
}
