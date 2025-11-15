package com.example.club_deportivo.models

data class ActivityEnrollment(
    val id: Int,
    val userId: Int,
    val activityId: Int,
    val activityName: String,
    val activityInstructor: String,
    val activitySchedule: String
)
