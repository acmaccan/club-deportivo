package com.example.club_deportivo.models

object UserRepository {
    fun getUsers(): List<UserData> {
        return listOf(
            UserData(
                1,
                "Admin",
                "admin@sportclub.com",
                "admin123456",
                UserRole.ADMIN
            ),
            ClientData(
                2,
                "María Gómez",
                "mg@gmail.com",
                "mg123456",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.DUE_SOON
            ),
            ClientData(
                3,
                "Carlos Ruiz",
                "cr@gmail.com",
                "cr123456",
                MembershipType.MEMBER,
                "$35000",
                PaymentStatus.OVERDUE
            ),
            ClientData(
                4,
                "Ana Torres",
                "at@gmail.com",
                "at123456",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.PAID
            ),
            ClientData(
                5,
                "Luis Fernández",
                "lf@gmail.com",
                "lf123456",
                MembershipType.MEMBER,
                "$15000",
                PaymentStatus.DUE_SOON
            ),
            ClientData(
                6,
                "Elena Moreno",
                "em@gmail.com",
                "em123456",
                MembershipType.NO_MEMBER,
                "$24000",
                PaymentStatus.OVERDUE
            ),
            ClientData(
                7,
                "David Jiménez",
                "dj@gmail.com",
                "dj123456",
                MembershipType.MEMBER,
                "$15000",
                PaymentStatus.PAID
            ),
            ClientData(
                8,
                "Laura Navarro",
                "ln@gmail.com",
                "ln123456",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.DUE_SOON
            ),
            ClientData(
                9,
                "Pedro Romero",
                "pr@gmail.com",
                "pr123456",
                MembershipType.MEMBER,
                "$50000",
                PaymentStatus.OVERDUE
            ),
            ClientData(
                10,
                "Sofía Castillo",
                "sc@gmail.com",
                "sc123456",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.PAID
            )
        )
    }

    fun findUserByCredentials(email: String, password: String): UserData? {
        return getUsers().find { it.email.equals(email, ignoreCase = true) && it.password == password }
    }

    fun getClients(): List<ClientData> {
        return getUsers().filterIsInstance<ClientData>()
    }
}
