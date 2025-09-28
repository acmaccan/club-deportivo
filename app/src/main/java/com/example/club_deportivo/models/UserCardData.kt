package com.example.club_deportivo.models

data class UserCardData(
    val id: Int,
    val name: String,
    val phone: String,
    val membershipType: String,
    val amount: String,
    val status: PaymentStatus,
    val showPayButton: Boolean = false
)
