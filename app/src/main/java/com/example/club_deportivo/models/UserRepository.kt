package com.example.club_deportivo.models

object UserRepository {
    fun getUsers(): List<UserCardData> {
        return listOf(
            UserCardData(1, "Juan Pérez", "jp@gmail.com", MembershipType.MEMBER, "$15000", PaymentStatus.PAID),
            UserCardData(2, "María Gómez", "mg@gmail.com", MembershipType.NO_MEMBER, "$12000", PaymentStatus.DUE_SOON),
            UserCardData(3, "Carlos Ruiz", "cr@gmail.com", MembershipType.MEMBER, "$35000", PaymentStatus.OVERDUE, showPayButton = true),
            UserCardData(4, "Ana Torres", "at@gmail.com", MembershipType.NO_MEMBER, "$12000", PaymentStatus.PAID),
            UserCardData(5, "Luis Fernández", "lf@gmail.com", MembershipType.MEMBER, "$15000", PaymentStatus.DUE_SOON),
            UserCardData(6, "Elena Moreno", "em@gmail.com", MembershipType.NO_MEMBER, "$24000", PaymentStatus.OVERDUE, showPayButton = true),
            UserCardData(7, "David Jiménez", "dj@gmail.com", MembershipType.MEMBER, "$15000", PaymentStatus.PAID),
            UserCardData(8, "Laura Navarro", "ln@gmail.com", MembershipType.NO_MEMBER, "$12000", PaymentStatus.DUE_SOON),
            UserCardData(9, "Pedro Romero", "pr@gmail.com", MembershipType.MEMBER, "$50000", PaymentStatus.OVERDUE, showPayButton = true),
            UserCardData(10, "Sofía Castillo", "sc@gmail.com", MembershipType.NO_MEMBER, "$12000", PaymentStatus.PAID)
        )
    }
}
