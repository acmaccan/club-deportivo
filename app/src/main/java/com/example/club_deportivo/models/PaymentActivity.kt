package com.example.club_deportivo.models

data class PaymentActivity(
    val id: Int,
    val name: String,
    val instructor: String,
    val schedule: String,
    val monthlyPrice: Int
)