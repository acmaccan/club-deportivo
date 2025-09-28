package com.example.club_deportivo.models

enum class MembershipType(
    val displayName: String,
    val description: String,
) {
    MEMBER("Pase Libre", "Acceso a todas las actividades"),
    NO_MEMBER("Pase Actividades", "Acceso solo a actividades específicas"),
}