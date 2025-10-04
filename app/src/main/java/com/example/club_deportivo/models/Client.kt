package com.example.club_deportivo.models

class Client(
    id: Int,
    name: String,
    email: String,
    password: String,

    val membershipType: MembershipType,
    val amount: String,
    val status: PaymentStatus,
    val hasValidMedicalAptitude: Boolean
) : User(
    id,
    name,
    email,
    password,
    UserRole.CLIENT,
)

