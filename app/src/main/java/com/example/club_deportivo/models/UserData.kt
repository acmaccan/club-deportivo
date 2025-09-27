package com.example.club_deportivo.models

import com.example.club_deportivo.ui.TagStatus

data class UserData(
    val id: Int,
    val cardId: Int,
    val name: String,
    val phone: String,
    val membership: String,
    val amount: String,
    val status: TagStatus,
    val showPayButton: Boolean = false
)
