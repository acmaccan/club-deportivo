package com.example.club_deportivo.models

import androidx.annotation.DrawableRes
import com.example.club_deportivo.R

data class Activity(
    val id: Int,
    val name: String,
    val instructor: String,
    val schedule: String,
    val monthlyPrice: Int,
    val duration: Int,
    val level: ActivityLevel,
    val room: String,
    val description: String? = null,
    val maxCapacity: Int? = null,
    val isActive: Boolean = true
) {
    val imageResId: Int
        @DrawableRes
        get() = when (name.lowercase()) {
            "yoga" -> R.drawable.activity_yoga
            "meditación" -> R.drawable.activity_meditation
            "pilates" -> R.drawable.activity_pilates
            else -> R.drawable.activity_yoga
        }
}
