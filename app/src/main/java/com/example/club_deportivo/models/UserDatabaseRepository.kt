package com.example.club_deportivo.models

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.club_deportivo.helpers.DatabaseHelper
import java.util.Calendar
import java.util.Locale

class UserDatabaseRepository (context: Context) {
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("Range")
    private fun mapCursorToClient(c: Cursor): Client {
        return Client(
            id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_ID)),
            name = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_USER_FULL_NAME)),
            email = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL)),
            password = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_USER_PASSWORD)),
            document = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_DOCUMENT)),
            hasValidMedicalAptitude = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE)) == 1,
            membershipType = MembershipType.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_MEMBERSHIP_TYPE))),
            amount = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_MEMBERSHIP_AMOUNT)),
            status = PaymentStatus.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_MEMBERSHIP_PAYMENT_STATUS))),
            expirationDate = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_MEMBERSHIP_EXPIRATION_DATE))
        )
    }

    @SuppressLint("Range")
    private fun mapCursorToAdmin(c: Cursor): Admin {
        return Admin(
            id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
            name = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_USER_FULL_NAME)),
            email = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL)),
            password = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_USER_PASSWORD))
        )
    }

    private val BASE_CLIENT_QUERY = """
        SELECT * FROM ${DatabaseHelper.TABLE_USERS} u
        JOIN ${DatabaseHelper.TABLE_CLIENTS} c ON u.${DatabaseHelper.COLUMN_USER_ID} = c.${DatabaseHelper.COLUMN_CLIENT_USER_ID}
        JOIN ${DatabaseHelper.TABLE_MEMBERSHIPS} m ON c.${DatabaseHelper.COLUMN_CLIENT_ID} = m.${DatabaseHelper.COLUMN_MEMBERSHIP_CLIENT_ID}
    """.trimIndent()

    private fun findAdminById(userId: Int, db: SQLiteDatabase): Admin? {
        val sql = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USER_ID} = ?"
        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                return mapCursorToAdmin(it)
            }
        }
        return null
    }

    fun findClientById(userId: Int, db: SQLiteDatabase? = null): Client? {
        val database = db ?: dbHelper.readableDatabase
        var client: Client? = null
        val sql = "$BASE_CLIENT_QUERY WHERE u.${DatabaseHelper.COLUMN_USER_ID} = ?"
        val cursor = database.rawQuery(sql, arrayOf(userId.toString()))

        cursor.use { c ->
            if (c.moveToFirst()) {
                client = mapCursorToClient(c)
            }
        }

        if (db == null) {
            database.close()
        }
        return client
    }

    /**
     * Busca un usuario por ID, determinando su tipo (Admin o Client).
     */
    fun findUserById(userId: Int): User? {
        val db = dbHelper.readableDatabase
        val roleCursor = db.query(DatabaseHelper.TABLE_USERS, arrayOf(DatabaseHelper.COLUMN_USER_ROLE), "${DatabaseHelper.COLUMN_USER_ID} = ?", arrayOf(userId.toString()), null, null, null)

        roleCursor.use {
            if (it.moveToFirst()) {
                val role = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE))

                return if (role == UserRole.ADMIN.name) {
                    findAdminById(userId, db)
                } else {
                    findClientById(userId, db)
                }
            }
        }
        return null
    }

    /**
     * Busca un usuario por credenciales. Lógica unificada.
     */
    fun findUserByCredentials(email: String, password: String): User? {
        val db = dbHelper.readableDatabase
        val userSql = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USER_EMAIL} = ? AND ${DatabaseHelper.COLUMN_USER_PASSWORD} = ? LIMIT 1"
        val userCursor = db.rawQuery(userSql, arrayOf(email, password))

        userCursor.use { uc ->
            if (uc.moveToFirst()) {
                val userId = uc.getInt(uc.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
                val userRole = uc.getString(uc.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE))

                return findUserById(userId)
            }
        }
        return null
    }

    /**
     * Obtiene la lista completa de todos los clientes.
     */
    fun getClients(): List<Client> {
        val db = dbHelper.readableDatabase
        val clients = mutableListOf<Client>()
        val cursor = db.rawQuery(BASE_CLIENT_QUERY, null)

        cursor.use { c ->
            while (c.moveToNext()) {
                clients.add(mapCursorToClient(c))
            }
        }
        db.close()
        return clients
    }

    /**
     * Obtiene los clientes con pagos vencidos.
     */
    fun getOverdueClients(): List<Client> {
        val db = dbHelper.readableDatabase
        val clients = mutableListOf<Client>()
        val sql = "$BASE_CLIENT_QUERY WHERE m.${DatabaseHelper.COLUMN_MEMBERSHIP_PAYMENT_STATUS} = ?"
        val cursor = db.rawQuery(sql, arrayOf(PaymentStatus.OVERDUE.name))

        cursor.use { c ->
            while (c.moveToNext()) {
                clients.add(mapCursorToClient(c))
            }
        }
        db.close()
        return clients
    }

    /**
     * Calcula el monto total de los pagos vencidos.
     */
    fun getTotalOverdueAmount(): Double {
        val db = dbHelper.readableDatabase
        var totalAmount = 0.0

        val sql = """
            SELECT SUM(REPLACE(m.${DatabaseHelper.COLUMN_MEMBERSHIP_AMOUNT}, '$', '')) 
            FROM ${DatabaseHelper.TABLE_MEMBERSHIPS} m
            WHERE m.${DatabaseHelper.COLUMN_MEMBERSHIP_PAYMENT_STATUS} = ?
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(PaymentStatus.OVERDUE.name))

        cursor.use { c ->
            if (c.moveToFirst()) {
                totalAmount = c.getDouble(0)
            }
        }
        db.close()
        return totalAmount
    }

    /**
     * Busca un cliente específico por su ID de usuario.
     */
    fun findClientById(userId: Int): Client? {
        val db = dbHelper.readableDatabase
        var client: Client? = null
        val sql = "$BASE_CLIENT_QUERY WHERE u.${DatabaseHelper.COLUMN_USER_ID} = ?"
        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))

        cursor.use { c ->
            if (c.moveToFirst()) {
                client = mapCursorToClient(c)
            }
        }
        db.close()
        return client
    }

    /**
     * Actualiza el estado del apto médico de un cliente.
     */
    fun updateMedicalAptitude(userId: Int, hasValidAptitude: Boolean): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE, if (hasValidAptitude) 1 else 0)
        }
        val updatedRows = db.update(DatabaseHelper.TABLE_CLIENTS, values, "${DatabaseHelper.COLUMN_CLIENT_USER_ID} = ?", arrayOf(userId.toString()))
        db.close()
        return updatedRows > 0
    }

    /**
     * Verifica si un email ya existe en la tabla de usuarios.
     */
    fun emailExists(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val sql = "SELECT 1 FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USER_EMAIL} = ? LIMIT 1"
        val cursor = db.rawQuery(sql, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    /**
     * Crea un nuevo cliente y lo agrega a la base de datos.
     */
    fun createNewClient(
        fullName: String,
        document: String,
        email: String,
        password: String,
        membershipType: MembershipType
    ): Long {
        val db = dbHelper.writableDatabase
        db.beginTransaction()

        var newUserId = -1L

        try {
            val userValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_USER_FULL_NAME, fullName)
                put(DatabaseHelper.COLUMN_USER_EMAIL, email)
                put(DatabaseHelper.COLUMN_USER_PASSWORD, password)
                put(DatabaseHelper.COLUMN_USER_ROLE, UserRole.CLIENT.name)
            }
            newUserId = db.insert(DatabaseHelper.TABLE_USERS, null, userValues)
            if (newUserId == -1L) {
                Log.e("UserDbRepository", "Falló la inserción en la tabla users.")
                return -1L
            }

            val clientValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_CLIENT_USER_ID, newUserId)
                put(DatabaseHelper.COLUMN_CLIENT_DOCUMENT, document)
                put(DatabaseHelper.COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE, 0)
            }
            val newClientId = db.insert(DatabaseHelper.TABLE_CLIENTS, null, clientValues)
            if (newClientId == -1L) {
                Log.e("UserDbRepository", "Falló la inserción en la tabla clients.")
                return -1L
            }

            val membershipValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_MEMBERSHIP_CLIENT_ID, newClientId)
                put(DatabaseHelper.COLUMN_MEMBERSHIP_TYPE, membershipType.name)
                put(DatabaseHelper.COLUMN_MEMBERSHIP_AMOUNT, "$0")
                put(DatabaseHelper.COLUMN_MEMBERSHIP_PAYMENT_STATUS, PaymentStatus.OVERDUE.name)
                put(DatabaseHelper.COLUMN_MEMBERSHIP_EXPIRATION_DATE, calculateExpirationDate())
            }
            val newMembershipId = db.insert(DatabaseHelper.TABLE_MEMBERSHIPS, null, membershipValues)
            if (newMembershipId == -1L) {
                Log.e("UserDbRepository", "Falló la inserción en la tabla memberships.")
                return -1L
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("UserDbRepository", "Error creando nuevo cliente en transacción", e)
            newUserId = -1L
        } finally {
            db.endTransaction()
            db.close()
        }

        return newUserId
    }

    /**
     * Calcula la fecha de expiración un año desde la fecha actual.
     */
    private fun calculateExpirationDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, 1)
        return String.format(Locale.US, "%04d-%02d-%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}