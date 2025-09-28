package com.example.club_deportivo.models

data class UserCardData(
    val id: Int,
    val name: String,
    val email: String,
    val membershipType: MembershipType,
    val amount: String,
    val status: PaymentStatus,
    val showPayButton: Boolean = false
)
