package com.example.club_deportivo.models

import com.example.club_deportivo.R

object ActivityRepository {
    fun getActivities(): List<Activity> {
        return listOf(
            Activity(
                id = 1,
                name = "Yoga",
                level = ActivityLevel.BEGINNER,
                duration = 30,
                imageResId = R.drawable.activity_yoga
            ),
            Activity(
                id = 2,
                name = "Meditación",
                level = ActivityLevel.ADVANCED,
                duration = 45,
                imageResId = R.drawable.activity_meditation
            ),
            Activity(
                id = 3,
                name = "Pilates",
                level = ActivityLevel.INTERMEDIATE,
                duration = 45,
                imageResId = R.drawable.activity_pilates
            )
        )
    }
}