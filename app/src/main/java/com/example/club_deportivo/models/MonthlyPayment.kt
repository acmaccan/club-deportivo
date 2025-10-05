package com.example.club_deportivo.models

data class MonthlyPayment(
    val month: String,
    val year: Int,
    val amount: Int,
    val dueDate: String,
    val daysUntilDue: Int,
    val description: String
)