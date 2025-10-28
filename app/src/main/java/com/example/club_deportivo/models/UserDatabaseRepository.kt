package com.example.club_deportivo.models

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.club_deportivo.helpers.DatabaseHelper
import com.example.club_deportivo.models.*
import java.util.Calendar
import java.util.Locale

class UserDatabaseRepository (context: Context) {
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("Range")
    private fun mapCursorToClient(c: Cursor): Client {
        return Client(
            id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
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

    private val BASE_CLIENT_QUERY = """
        SELECT * FROM ${DatabaseHelper.TABLE_USERS} u
        JOIN ${DatabaseHelper.TABLE_CLIENTS} c ON u.${DatabaseHelper.COLUMN_USER_ID} = c.${DatabaseHelper.COLUMN_CLIENT_USER_ID}
        JOIN ${DatabaseHelper.TABLE_MEMBERSHIPS} m ON c.${DatabaseHelper.COLUMN_CLIENT_ID} = m.${DatabaseHelper.COLUMN_MEMBERSHIP_CLIENT_ID}
    """.trimIndent()

    /**
     * Busca un usuario por su ID.
     */
    @SuppressLint("Range")
    fun findUserById(userId: Int): User? {
        val db = dbHelper.readableDatabase
        var user: User? = null

        // Primero intentamos buscarlo como si fuera un cliente completo
        val clientSql = "$BASE_CLIENT_QUERY WHERE u.${DatabaseHelper.COLUMN_USER_ID} = ?"
        val clientCursor = db.rawQuery(clientSql, arrayOf(userId.toString()))

        clientCursor.use { c ->
            if (c.moveToFirst()) {
                user = mapCursorToClient(c)
            }
        }

        // Si no se encontró como cliente (o la consulta no devolvió nada),
        // podría ser un Admin. Hacemos una segunda consulta más simple.
        if (user == null) {
            val adminSql = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USER_ID} = ? AND ${DatabaseHelper.COLUMN_USER_ROLE} = 'ADMIN'"
            val adminCursor = db.rawQuery(adminSql, arrayOf(userId.toString()))
            adminCursor.use { ac ->
                if (ac.moveToFirst()) {
                    user = User(
                        id = ac.getInt(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
                        name = ac.getString(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_FULL_NAME)),
                        email = ac.getString(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL)),
                        password = ac.getString(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_PASSWORD)),
                        role = UserRole.ADMIN
                    )
                }
            }
        }

        db.close()
        return user
    }

    /**
     * Busca un usuario por sus credenciales. Si es cliente, une las 3 tablas.
     */
    @SuppressLint("Range")
    fun findUserByCredentials(email: String, password: String): User? {
        val db = dbHelper.readableDatabase
        var user: User? = null

        val sql = "$BASE_CLIENT_QUERY WHERE u.${DatabaseHelper.COLUMN_USER_EMAIL} = ? AND u.${DatabaseHelper.COLUMN_USER_PASSWORD} = ?"
        val cursor = db.rawQuery(sql, arrayOf(email, password))

        cursor.use { c ->
            if (c.moveToFirst()) {
                user = mapCursorToClient(c)
            }
        }

        // Si no se encontró como cliente, podría ser un admin
        if (user == null) {
            val adminSql = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USER_EMAIL} = ? AND ${DatabaseHelper.COLUMN_USER_PASSWORD} = ? AND ${DatabaseHelper.COLUMN_USER_ROLE} = 'ADMIN'"
            val adminCursor = db.rawQuery(adminSql, arrayOf(email, password))
            adminCursor.use { ac ->
                if (ac.moveToFirst()) {
                    user = User(
                        id = ac.getInt(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)),
                        name = ac.getString(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_FULL_NAME)),
                        email = ac.getString(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL)),
                        password = ac.getString(ac.getColumnIndex(DatabaseHelper.COLUMN_USER_PASSWORD)),
                        role = UserRole.ADMIN
                    )
                }
            }
        }

        db.close()
        return user
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
        // Añadimos una cláusula WHERE para que la BD haga el filtrado
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
        // El `user_id` en la tabla `clients` es la FK a la tabla `users`
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
    ): Client? {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            // 1. Insertar en la tabla `users`
            val userValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_USER_FULL_NAME, fullName)
                put(DatabaseHelper.COLUMN_USER_EMAIL, email)
                put(DatabaseHelper.COLUMN_USER_PASSWORD, password)
                put(DatabaseHelper.COLUMN_USER_ROLE, UserRole.CLIENT.name)
            }
            val newUserId = db.insert(DatabaseHelper.TABLE_USERS, null, userValues)
            if (newUserId == -1L) {
                Log.e("UserDbRepository", "Fallo al insertar en la tabla users")
                return null // Fallo en la inserción
            }

            // 2. Insertar en la tabla `clients`
            val clientValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_CLIENT_USER_ID, newUserId)
                put(DatabaseHelper.COLUMN_CLIENT_DOCUMENT, document)
                put(DatabaseHelper.COLUMN_CLIENT_HAS_VALID_MEDICAL_APTITUDE, 0) // Por defecto es falso
            }
            val newClientId = db.insert(DatabaseHelper.TABLE_CLIENTS, null, clientValues)
            if (newClientId == -1L) {
                Log.e("UserDbRepository", "Fallo al insertar en la tabla clients")
                return null // Fallo en la inserción
            }

            // 3. Insertar en la tabla `memberships`
            val membershipValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_MEMBERSHIP_CLIENT_ID, newClientId)
                put(DatabaseHelper.COLUMN_MEMBERSHIP_TYPE, membershipType.name)
                put(DatabaseHelper.COLUMN_MEMBERSHIP_AMOUNT, "$0") // Valor inicial
                put(DatabaseHelper.COLUMN_MEMBERSHIP_PAYMENT_STATUS, PaymentStatus.OVERDUE.name) // Se debe pagar
                put(DatabaseHelper.COLUMN_MEMBERSHIP_EXPIRATION_DATE, calculateExpirationDate())
            }
            val newMembershipId = db.insert(DatabaseHelper.TABLE_MEMBERSHIPS, null, membershipValues)
            if (newMembershipId == -1L) {
                Log.e("UserDbRepository", "Fallo al insertar en la tabla memberships")
                return null // Fallo en la inserción
            }

            db.setTransactionSuccessful() // Marcar la transacción como exitosa

            // Devolver el objeto Client recién creado
            return findClientById(newUserId.toInt())

        } catch (e: Exception) {
            Log.e("UserDbRepository", "Error creando nuevo cliente en transacción", e)
            return null
        }
        finally {
            db.endTransaction()
            db.close()
        }
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