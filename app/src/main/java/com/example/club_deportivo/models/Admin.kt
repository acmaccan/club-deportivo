package com.example.club_deportivo.models

class Admin(
    id: Int,
    name: String,
    email: String,
    password: String
) : User(
    id,
    name,
    email,
    password,
    UserRole.ADMIN
)