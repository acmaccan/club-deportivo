package com.example.club_deportivo.models

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.club_deportivo.R
import com.example.club_deportivo.helpers.DatabaseHelper

class ActivityDatabaseRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    /**
     * Mapea un Cursor a un objeto PaymentActivity.
     */
    @SuppressLint("Range")
    private fun mapCursorToPaymentActivity(c: Cursor): PaymentActivity {
        return PaymentActivity(
            id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_ID)),
            name = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_NAME)),
            instructor = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_INSTRUCTOR)) ?: "",
            schedule = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_SCHEDULE)) ?: "",
            monthlyPrice = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_MONTHLY_PRICE))
        )
    }

    /**
     * Mapea un Cursor a un objeto Activity.
     */
    @SuppressLint("Range")
    private fun mapCursorToActivity(c: Cursor): Activity {
        return Activity(
            id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_ID)),
            name = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_NAME)),
            instructor = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_INSTRUCTOR)) ?: "",
            schedule = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_SCHEDULE)) ?: "",
            monthlyPrice = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_MONTHLY_PRICE)),
            duration = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_DURATION)),
            level = ActivityLevel.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_LEVEL))),
            description = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_DESCRIPTION)),
            maxCapacity = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_MAX_CAPACITY)),
            isActive = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_IS_ACTIVE)) == 1
        )
    }

    /**
     * Obtiene todas las actividades activas para mostrar en la UI.
     */
    fun getActivitiesForUI(): List<Activity> {
        val activities = mutableListOf<Activity>()
        val db = dbHelper.readableDatabase

        val sql = """
            SELECT * FROM ${DatabaseHelper.TABLE_ACTIVITIES}
            WHERE ${DatabaseHelper.COLUMN_ACTIVITY_IS_ACTIVE} = 1
            ORDER BY ${DatabaseHelper.COLUMN_ACTIVITY_NAME}
        """.trimIndent()

        val cursor = db.rawQuery(sql, null)

        cursor.use {
            while (it.moveToNext()) {
                activities.add(mapCursorToActivity(it))
            }
        }

        db.close()
        return activities
    }

    /**
     * Obtiene todas las actividades activas.
     */
    fun getActivities(): List<PaymentActivity> {
        val activities = mutableListOf<PaymentActivity>()
        val db = dbHelper.readableDatabase

        val sql = """
            SELECT * FROM ${DatabaseHelper.TABLE_ACTIVITIES}
            WHERE ${DatabaseHelper.COLUMN_ACTIVITY_IS_ACTIVE} = 1
            ORDER BY ${DatabaseHelper.COLUMN_ACTIVITY_NAME}
        """.trimIndent()

        val cursor = db.rawQuery(sql, null)

        cursor.use {
            while (it.moveToNext()) {
                activities.add(mapCursorToPaymentActivity(it))
            }
        }

        db.close()
        return activities
    }

    /**
     * Busca una actividad por su ID.
     */
    fun getActivityById(activityId: Int): PaymentActivity? {
        val db = dbHelper.readableDatabase
        var activity: PaymentActivity? = null

        val sql = """
            SELECT * FROM ${DatabaseHelper.TABLE_ACTIVITIES}
            WHERE ${DatabaseHelper.COLUMN_ACTIVITY_ID} = ?
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(activityId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                activity = mapCursorToPaymentActivity(it)
            }
        }

        db.close()
        return activity
    }

    /**
     * Crea una nueva actividad.
     * @return ID de la actividad creada, o -1 si hay error
     */
    fun createActivity(
        name: String,
        instructor: String,
        schedule: String,
        monthlyPrice: Int,
        duration: Int,
        level: ActivityLevel,
        description: String,
        maxCapacity: Int,
        isActive: Boolean = true
    ): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ACTIVITY_NAME, name)
            put(DatabaseHelper.COLUMN_ACTIVITY_INSTRUCTOR, instructor)
            put(DatabaseHelper.COLUMN_ACTIVITY_SCHEDULE, schedule)
            put(DatabaseHelper.COLUMN_ACTIVITY_MONTHLY_PRICE, monthlyPrice)
            put(DatabaseHelper.COLUMN_ACTIVITY_DESCRIPTION, description)
            put(DatabaseHelper.COLUMN_ACTIVITY_MAX_CAPACITY, maxCapacity)
            put(DatabaseHelper.COLUMN_ACTIVITY_IS_ACTIVE, if (isActive) 1 else 0)
            put(DatabaseHelper.COLUMN_ACTIVITY_DURATION, duration)
            put(DatabaseHelper.COLUMN_ACTIVITY_LEVEL, level.name)
        }

        val id = db.insert(DatabaseHelper.TABLE_ACTIVITIES, null, values)
        db.close()
        return id
    }

    /**
     * Actualiza una actividad existente.
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun updateActivity(
        activityId: Int,
        name: String,
        instructor: String,
        schedule: String,
        monthlyPrice: Int,
        duration: Int,
        level: ActivityLevel,
        description: String,
        maxCapacity: Int
    ): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ACTIVITY_NAME, name)
            put(DatabaseHelper.COLUMN_ACTIVITY_INSTRUCTOR, instructor)
            put(DatabaseHelper.COLUMN_ACTIVITY_SCHEDULE, schedule)
            put(DatabaseHelper.COLUMN_ACTIVITY_MONTHLY_PRICE, monthlyPrice)
            put(DatabaseHelper.COLUMN_ACTIVITY_DESCRIPTION, description)
            put(DatabaseHelper.COLUMN_ACTIVITY_MAX_CAPACITY, maxCapacity)
            put(DatabaseHelper.COLUMN_ACTIVITY_DURATION, duration)
            put(DatabaseHelper.COLUMN_ACTIVITY_LEVEL, level.name)
        }

        val rowsAffected = db.update(
            DatabaseHelper.TABLE_ACTIVITIES,
            values,
            "${DatabaseHelper.COLUMN_ACTIVITY_ID} = ?",
            arrayOf(activityId.toString())
        )

        db.close()
        return rowsAffected > 0
    }

    /**
     * Activa o desactiva una actividad.
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun toggleActivityStatus(activityId: Int, isActive: Boolean): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ACTIVITY_IS_ACTIVE, if (isActive) 1 else 0)
        }

        val rowsAffected = db.update(
            DatabaseHelper.TABLE_ACTIVITIES,
            values,
            "${DatabaseHelper.COLUMN_ACTIVITY_ID} = ?",
            arrayOf(activityId.toString())
        )

        db.close()
        return rowsAffected > 0
    }

    /**
     * Inscribe a un usuario en una actividad.
     * @return ID de la inscripción creada, o -1 si hay error
     */
    fun enrollUserToActivity(userId: Int, activityId: Int, status: String = "active"): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ENROLLMENT_USER_ID, userId)
            put(DatabaseHelper.COLUMN_ENROLLMENT_ACTIVITY_ID, activityId)
            put(DatabaseHelper.COLUMN_ENROLLMENT_STATUS, status)
        }

        val id = db.insert(DatabaseHelper.TABLE_ACTIVITY_ENROLLMENTS, null, values)
        db.close()
        return id
    }

    /**
     * Obtiene todas las inscripciones de un usuario.
     */
    @SuppressLint("Range")
    fun getUserEnrollments(userId: Int): List<ActivityEnrollment> {
        val enrollments = mutableListOf<ActivityEnrollment>()
        val db = dbHelper.readableDatabase

        val sql = """
            SELECT
                ae.${DatabaseHelper.COLUMN_ENROLLMENT_ID},
                ae.${DatabaseHelper.COLUMN_ENROLLMENT_USER_ID},
                ae.${DatabaseHelper.COLUMN_ENROLLMENT_ACTIVITY_ID},
                ae.${DatabaseHelper.COLUMN_ENROLLMENT_STATUS},
                a.${DatabaseHelper.COLUMN_ACTIVITY_NAME},
                a.${DatabaseHelper.COLUMN_ACTIVITY_INSTRUCTOR},
                a.${DatabaseHelper.COLUMN_ACTIVITY_SCHEDULE}
            FROM ${DatabaseHelper.TABLE_ACTIVITY_ENROLLMENTS} ae
            JOIN ${DatabaseHelper.TABLE_ACTIVITIES} a
                ON ae.${DatabaseHelper.COLUMN_ENROLLMENT_ACTIVITY_ID} = a.${DatabaseHelper.COLUMN_ACTIVITY_ID}
            WHERE ae.${DatabaseHelper.COLUMN_ENROLLMENT_USER_ID} = ?
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(userId.toString()))

        cursor.use {
            while (it.moveToNext()) {
                enrollments.add(
                    ActivityEnrollment(
                        id = it.getInt(it.getColumnIndex(DatabaseHelper.COLUMN_ENROLLMENT_ID)),
                        userId = it.getInt(it.getColumnIndex(DatabaseHelper.COLUMN_ENROLLMENT_USER_ID)),
                        activityId = it.getInt(it.getColumnIndex(DatabaseHelper.COLUMN_ENROLLMENT_ACTIVITY_ID)),
                        status = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_ENROLLMENT_STATUS)),
                        activityName = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_NAME)),
                        activityInstructor = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_INSTRUCTOR)),
                        activitySchedule = it.getString(it.getColumnIndex(DatabaseHelper.COLUMN_ACTIVITY_SCHEDULE))
                    )
                )
            }
        }

        db.close()
        return enrollments
    }

    /**
     * Actualiza el estado de una inscripción.
     * @return true si se actualizó correctamente, false en caso contrario
     */
    fun updateEnrollmentStatus(enrollmentId: Int, status: String): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ENROLLMENT_STATUS, status)
        }

        val rowsAffected = db.update(
            DatabaseHelper.TABLE_ACTIVITY_ENROLLMENTS,
            values,
            "${DatabaseHelper.COLUMN_ENROLLMENT_ID} = ?",
            arrayOf(enrollmentId.toString())
        )

        db.close()
        return rowsAffected > 0
    }

    /**
     * Verifica si un usuario está inscrito en una actividad.
     */
    fun isUserEnrolled(userId: Int, activityId: Int): Boolean {
        val db = dbHelper.readableDatabase

        val sql = """
            SELECT COUNT(*) FROM ${DatabaseHelper.TABLE_ACTIVITY_ENROLLMENTS}
            WHERE ${DatabaseHelper.COLUMN_ENROLLMENT_USER_ID} = ?
            AND ${DatabaseHelper.COLUMN_ENROLLMENT_ACTIVITY_ID} = ?
            AND ${DatabaseHelper.COLUMN_ENROLLMENT_STATUS} = 'active'
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(userId.toString(), activityId.toString()))
        var isEnrolled = false

        cursor.use {
            if (it.moveToFirst()) {
                isEnrolled = it.getInt(0) > 0
            }
        }

        db.close()
        return isEnrolled
    }
}
