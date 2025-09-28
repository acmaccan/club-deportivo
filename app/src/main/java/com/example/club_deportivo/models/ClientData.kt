package com.example.club_deportivo.models

class ClientData(
    id: Int,
    name: String,
    email: String,
    password: String,

    val membershipType: MembershipType,
    val amount: String,
    val status: PaymentStatus
) : UserData(
    id,
    name,
    email,
    password,
    UserRole.CLIENT,
)

