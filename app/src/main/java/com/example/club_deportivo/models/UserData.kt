package com.example.club_deportivo.models

open class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val userType: UserRole,
)

