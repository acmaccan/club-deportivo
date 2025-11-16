package com.example.club_deportivo.models

data class Payment(
    val id: Int,
    val clientId: Int,
    val type: PaymentType,
    val amount: Int,
    val periodMonth: String,
    val periodYear: Int,
    val dueDate: String,
    val paymentDate: String?,
    val status: TransactionStatus,
    val activityId: Int?
)

enum class PaymentType {
    MONTHLY_FEE,
    ACTIVITY_FEE
}

enum class TransactionStatus {
    PENDING,
    SUCCESS,
    ERROR
}
