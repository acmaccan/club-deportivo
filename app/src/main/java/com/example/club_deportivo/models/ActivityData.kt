package com.example.club_deportivo.models

import androidx.annotation.DrawableRes

data class ActivityData(
    val id: Int,
    val name: String,
    val level: ActivityLevel,
    val durationMinutes: Int,
    @DrawableRes val imageResId: Int
)
