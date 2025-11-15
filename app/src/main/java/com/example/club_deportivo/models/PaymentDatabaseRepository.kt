package com.example.club_deportivo.models

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.club_deportivo.helpers.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PaymentDatabaseRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    @SuppressLint("Range")
    private fun mapCursorToPayment(c: Cursor): Payment {
        return Payment(
            id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_ID)),
            clientId = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_CLIENT_ID)),
            type = PaymentType.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_TYPE)).uppercase()),
            amount = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_AMOUNT)),
            periodMonth = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_PERIOD_MONTH)),
            periodYear = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_PERIOD_YEAR)),
            dueDate = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_DUE_DATE)),
            paymentDate = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_PAYMENT_DATE)),
            status = TransactionStatus.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_STATUS)).uppercase()),
            activityId = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_ACTIVITY_ID)).let {
                if (c.isNull(c.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_ACTIVITY_ID))) null else it
            }
        )
    }

    fun getPendingMonthlyPaymentForClient(clientId: Int): Payment? {
        val db = dbHelper.readableDatabase
        var payment: Payment? = null

        val sql = """
            SELECT * FROM ${DatabaseHelper.TABLE_PAYMENTS}
            WHERE ${DatabaseHelper.COLUMN_PAYMENT_CLIENT_ID} = ?
            AND ${DatabaseHelper.COLUMN_PAYMENT_TYPE} = 'monthly_fee'
            AND ${DatabaseHelper.COLUMN_PAYMENT_STATUS} = 'pending'
            ORDER BY ${DatabaseHelper.COLUMN_PAYMENT_DUE_DATE} ASC
            LIMIT 1
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(clientId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                payment = mapCursorToPayment(it)
            }
        }

        db.close()
        return payment
    }

    fun createPayment(
        clientId: Int,
        type: PaymentType,
        amount: Int,
        periodMonth: String,
        periodYear: Int,
        dueDate: String,
        status: TransactionStatus,
        activityId: Int? = null
    ): Long {
        val db = dbHelper.writableDatabase

        if (status == TransactionStatus.SUCCESS && type == PaymentType.MONTHLY_FEE) {
            val pendingPayment = getPendingMonthlyPaymentForClient(clientId)
            if (pendingPayment != null) {
                updatePaymentStatus(pendingPayment.id, TransactionStatus.SUCCESS)
                db.close()
                return pendingPayment.id.toLong()
            }
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PAYMENT_CLIENT_ID, clientId)
            put(DatabaseHelper.COLUMN_PAYMENT_TYPE, type.name.lowercase())
            put(DatabaseHelper.COLUMN_PAYMENT_AMOUNT, amount)
            put(DatabaseHelper.COLUMN_PAYMENT_PERIOD_MONTH, periodMonth)
            put(DatabaseHelper.COLUMN_PAYMENT_PERIOD_YEAR, periodYear)
            put(DatabaseHelper.COLUMN_PAYMENT_DUE_DATE, dueDate)
            put(DatabaseHelper.COLUMN_PAYMENT_STATUS, status.name.lowercase())
            if (status == TransactionStatus.SUCCESS) {
                put(DatabaseHelper.COLUMN_PAYMENT_PAYMENT_DATE, currentDate)
            }
            if (activityId != null) {
                put(DatabaseHelper.COLUMN_PAYMENT_ACTIVITY_ID, activityId)
            }
        }

        val id = db.insert(DatabaseHelper.TABLE_PAYMENTS, null, values)

        if (status == TransactionStatus.SUCCESS && type == PaymentType.MONTHLY_FEE) {
            updateMembershipStatus(db, clientId)
        }

        db.close()
        return id
    }

    fun updatePaymentStatus(paymentId: Int, status: TransactionStatus): Boolean {
        val db = dbHelper.writableDatabase

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PAYMENT_STATUS, status.name.lowercase())
            if (status == TransactionStatus.SUCCESS) {
                put(DatabaseHelper.COLUMN_PAYMENT_PAYMENT_DATE, currentDate)
            }
        }

        val rowsAffected = db.update(
            DatabaseHelper.TABLE_PAYMENTS,
            values,
            "${DatabaseHelper.COLUMN_PAYMENT_ID} = ?",
            arrayOf(paymentId.toString())
        )

        if (status == TransactionStatus.SUCCESS && rowsAffected > 0) {
            val clientIdQuery = db.rawQuery(
                "SELECT ${DatabaseHelper.COLUMN_PAYMENT_CLIENT_ID} FROM ${DatabaseHelper.TABLE_PAYMENTS} WHERE ${DatabaseHelper.COLUMN_PAYMENT_ID} = ?",
                arrayOf(paymentId.toString())
            )
            clientIdQuery.use {
                if (it.moveToFirst()) {
                    val clientId = it.getInt(0)
                    updateMembershipStatus(db, clientId)
                }
            }
        }

        db.close()
        return rowsAffected > 0
    }

    private fun updateMembershipStatus(db: android.database.sqlite.SQLiteDatabase, clientId: Int) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        val newExpirationDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_MEMBERSHIP_PAYMENT_STATUS, PaymentStatus.PAID.name)
            put(DatabaseHelper.COLUMN_MEMBERSHIP_EXPIRATION_DATE, newExpirationDate)
        }

        db.update(
            DatabaseHelper.TABLE_MEMBERSHIPS,
            values,
            "${DatabaseHelper.COLUMN_MEMBERSHIP_CLIENT_ID} = ?",
            arrayOf(clientId.toString())
        )
    }

    fun calculateDaysUntilDue(dueDate: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val due = sdf.parse(dueDate)
        val today = Calendar.getInstance().time

        val diffInMillis = due.time - today.time
        return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
    }

    fun getLastSuccessfulMonthlyPaymentForClient(clientId: Int): Payment? {
        val db = dbHelper.readableDatabase
        var payment: Payment? = null

        val sql = """
            SELECT * FROM ${DatabaseHelper.TABLE_PAYMENTS}
            WHERE ${DatabaseHelper.COLUMN_PAYMENT_CLIENT_ID} = ?
            AND ${DatabaseHelper.COLUMN_PAYMENT_TYPE} = 'monthly_fee'
            AND ${DatabaseHelper.COLUMN_PAYMENT_STATUS} = 'success'
            ORDER BY ${DatabaseHelper.COLUMN_PAYMENT_PAYMENT_DATE} DESC
            LIMIT 1
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(clientId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                payment = mapCursorToPayment(it)
            }
        }

        db.close()
        return payment
    }

    fun getMonthlyPaymentForUI(clientId: Int, isPending: Boolean = true): MonthlyPayment? {
        val payment = if (isPending) {
            getPendingMonthlyPaymentForClient(clientId)
        } else {
            getLastSuccessfulMonthlyPaymentForClient(clientId)
        } ?: return null

        return MonthlyPayment(
            month = payment.periodMonth,
            year = payment.periodYear,
            amount = payment.amount,
            dueDate = payment.dueDate,
            daysUntilDue = calculateDaysUntilDue(payment.dueDate),
            description = "Pase libre - Acceso completo"
        )
    }
}
