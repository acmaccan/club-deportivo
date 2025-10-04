package com.example.club_deportivo.models

import androidx.annotation.DrawableRes

data class Activity(
    val id: Int,
    val name: String,
    val level: ActivityLevel,
    val duration: Int,
    @DrawableRes val imageResId: Int,
    val day: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val instructor: String
)
