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
                imageResId = R.drawable.activity_yoga,
                day = "Lunes",
                startTime = "10:00",
                endTime = "11:30",
                room = "Sala 1",
                instructor = "María"
            ),
            Activity(
                id = 2,
                name = "Meditación",
                level = ActivityLevel.ADVANCED,
                duration = 45,
                imageResId = R.drawable.activity_meditation,
                day = "Martes",
                startTime = "14:00",
                endTime = "15:30",
                room = "Sala 2",
                instructor = "Juan"
            ),
            Activity(
                id = 3,
                name = "Pilates",
                level = ActivityLevel.INTERMEDIATE,
                duration = 45,
                imageResId = R.drawable.activity_pilates,
                day = "Miércoles",
                startTime = "16:00",
                endTime = "17:30",
                room = "Sala 3",
                instructor = "Ana"
            )
        )
    }
}