package com.example.club_deportivo.models

data class UserCardData(
    val id: Int,
    val cardId: Int,
    val name: String,
    val phone: String,
    val membership: String,
    val amount: String,
    val status: PaymentStatus,
    val showPayButton: Boolean = false
)
