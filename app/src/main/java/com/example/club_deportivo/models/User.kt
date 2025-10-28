package com.example.club_deportivo.models

open class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole,
)

