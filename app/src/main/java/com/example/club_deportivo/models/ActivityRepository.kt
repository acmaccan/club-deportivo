package com.example.club_deportivo.models

import com.example.club_deportivo.R

object ActivityRepository {
    fun getActivities(): List<ActivityData> {
        return listOf(
            ActivityData(
                id = 1,
                name = "Yoga",
                level = ActivityLevel.BEGINNER,
                durationMinutes = 30,
                imageResId = R.drawable.activity_yoga
            ),
            ActivityData(
                id = 2,
                name = "Meditación",
                level = ActivityLevel.ADVANCED,
                durationMinutes = 45,
                imageResId = R.drawable.activity_meditation
            ),
            ActivityData(
                id = 3,
                name = "Pilates",
                level = ActivityLevel.INTERMEDIATE,
                durationMinutes = 45,
                imageResId = R.drawable.activity_pilates
            )
        )
    }
}