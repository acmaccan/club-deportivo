package com.example.club_deportivo.models

object PaymentRepository {
    
    fun getMonthlyPayment(): MonthlyPayment {
        return MonthlyPayment(
            month = "Septiembre",
            year = 2025,
            amount = 35000,
            dueDate = "30/09/2025",
            daysUntilDue = 8,
            description = "Pase libre - Acceso completo"
        )
    }
    
    fun getPaymentActivities(): List<PaymentActivity> {
        return listOf(
            PaymentActivity(
                id = 1,
                name = "Yoga",
                instructor = "María García",
                schedule = "Lu-Mie-Vie 8:00",
                monthlyPrice = 15000
            ),
            PaymentActivity(
                id = 2,
                name = "Spinning",
                instructor = "Carlos López",
                schedule = "Mar-Jue 19:00",
                monthlyPrice = 25000
            ),
            PaymentActivity(
                id = 3,
                name = "Pilates",
                instructor = "Ana Rodríguez",
                schedule = "Lu-Mie 18:00",
                monthlyPrice = 25000
            ),
            PaymentActivity(
                id = 4,
                name = "Natación",
                instructor = "Diego Martín",
                schedule = "Lu-Mie-Vie 20:00",
                monthlyPrice = 30000
            ),
            PaymentActivity(
                id = 5,
                name = "CrossFit",
                instructor = "Roberto Silva",
                schedule = "Mar-Jue-Sáb 20:00",
                monthlyPrice = 20000
            )
        )
    }
}