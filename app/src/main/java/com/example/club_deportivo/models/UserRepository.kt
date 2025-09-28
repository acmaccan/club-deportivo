package com.example.club_deportivo.models

object UserRepository {
    fun getUsers(): List<UserCardData> {
        return listOf(
            UserCardData(1, "Juan Pérez", "+54 11 5555-1234", "Pase Libre", "$15000", PaymentStatus.PAID),
            UserCardData(2, "María Gómez", "+54 11 5555-5678", "Actividades", "$12000", PaymentStatus.DUE_SOON),
            UserCardData(3, "Carlos Ruiz", "+54 11 5555-9012", "Pase Libre", "$35000", PaymentStatus.OVERDUE, showPayButton = true),
            UserCardData(4, "Ana Torres", "+54 11 3333-1111", "Actividades", "$12000", PaymentStatus.PAID),
            UserCardData(5, "Luis Fernández", "+54 11 4444-2222", "Pase Libre", "$15000", PaymentStatus.DUE_SOON),
            UserCardData(6, "Elena Moreno", "+54 11 6666-3333", "Actividades", "$24000", PaymentStatus.OVERDUE, showPayButton = true),
            UserCardData(7, "David Jiménez", "+54 11 7777-4444", "Pase Libre", "$15000", PaymentStatus.PAID),
            UserCardData(8, "Laura Navarro", "+54 11 8888-5555", "Actividades", "$12000", PaymentStatus.DUE_SOON),
            UserCardData(9, "Pedro Romero", "+54 11 9999-6666", "Pase Libre", "$50000", PaymentStatus.OVERDUE, showPayButton = true),
            UserCardData(10, "Sofía Castillo", "+54 11 2222-7777", "Actividades", "$12000", PaymentStatus.PAID)
        )
    }
}
