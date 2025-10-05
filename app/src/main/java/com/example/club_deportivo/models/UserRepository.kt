package com.example.club_deportivo.models

object UserRepository {
    private val users = mutableListOf<User>()
    
    init {
        users.addAll(getInitialUsers())
    }
    
    /**
     * Obtiene una copia inmutable de la lista de usuarios.
     */
    fun getUsers(): List<User> {
        return users.toList()
    }
    
    /**
     * Crea la lista inicial de usuarios del sistema.
     */
    private fun getInitialUsers(): List<User> {
        return listOf(
            User(
                1,
                "Admin",
                "admin@sportclub.com",
                "admin123456",
                UserRole.ADMIN
            ),
            Client(
                2,
                "María Gómez",
                "mg@gmail.com",
                "mg123456",
                "12345678",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.DUE_SOON,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-10-31"
            ),
            Client(
                3,
                "Carlos Ruiz",
                "cr@gmail.com",
                "cr123456",
                "23456789",
                MembershipType.MEMBER,
                "$35000",
                PaymentStatus.OVERDUE,
                hasValidMedicalAptitude = false,
                expirationDate = "2025-09-30"
            ),
            Client(
                4,
                "Ana Torres",
                "at@gmail.com",
                "at123456",
                "34567890",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.PAID,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-11-30"
            ),
            Client(
                5,
                "Luis Fernández",
                "lf@gmail.com",
                "lf123456",
                "45678901",
                MembershipType.MEMBER,
                "$15000",
                PaymentStatus.DUE_SOON,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-10-31"
            ),
            Client(
                6,
                "Elena Moreno",
                "em@gmail.com",
                "em123456",
                "56789012",
                MembershipType.NO_MEMBER,
                "$24000",
                PaymentStatus.OVERDUE,
                hasValidMedicalAptitude = false,
                expirationDate = "2025-09-30"
            ),
            Client(
                7,
                "David Jiménez",
                "dj@gmail.com",
                "dj123456",
                "67890123",
                MembershipType.MEMBER,
                "$15000",
                PaymentStatus.PAID,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-11-30"
            ),
            Client(
                8,
                "Laura Navarro",
                "ln@gmail.com",
                "ln123456",
                "78901234",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.DUE_SOON,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-10-31"
            ),
            Client(
                9,
                "Pedro Romero",
                "pr@gmail.com",
                "pr123456",
                "89012345",
                MembershipType.MEMBER,
                "$50000",
                PaymentStatus.OVERDUE,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-09-30"
            ),
            Client(
                10,
                "Sofía Castillo",
                "sc@gmail.com",
                "sc123456",
                "90123456",
                MembershipType.NO_MEMBER,
                "$12000",
                PaymentStatus.PAID,
                hasValidMedicalAptitude = true,
                expirationDate = "2025-11-30"
            )
        )
    }

    /**
     * Busca un usuario por su ID.
     */
    fun findUserById(id: Int): User? {
        return getUsers().find { it.id == id }
    }

    /**
     * Busca un usuario por email y contraseña para autenticación.
     */
    fun findUserByCredentials(email: String, password: String): User? {
        return getUsers().find { it.email.equals(email, ignoreCase = true) && it.password == password }
    }
    
    /**
     * Crea un nuevo cliente y lo agrega al repositorio.
     */
    fun createNewClient(
        fullName: String,
        document: String,
        email: String,
        password: String,
        membershipType: MembershipType
    ): Client {
        val newId = (getUsers().maxByOrNull { it.id }?.id ?: 0) + 1
        val newClient = Client(
            id = newId,
            name = fullName,
            document = document,
            email = email,
            password = password,
            membershipType = membershipType,
            amount = "$0",
            status = PaymentStatus.OVERDUE,
            hasValidMedicalAptitude = false,
            expirationDate = calculateExpirationDate()
        )
        users.add(newClient)
        return newClient
    }

    /**
     * Calcula la fecha de expiración un año desde la fecha actual.
     */
    private fun calculateExpirationDate(): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.YEAR, 1)
        return String.format(java.util.Locale.US, "%04d-%02d-%02d", 
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH) + 1,
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }
    
    /**
     * Verifica si un email ya está registrado en el sistema.
     */
    fun emailExists(email: String): Boolean {
        return getUsers().any { it.email.equals(email, ignoreCase = true) }
    }
    
    /**
     * Actualiza el estado del apto físico de un cliente.
     */
    fun updateMedicalAptitude(userId: Int, hasValidAptitude: Boolean): Boolean {
        val userIndex = users.indexOfFirst { it.id == userId }
        if (userIndex != -1 && users[userIndex] is Client) {
            val client = users[userIndex] as Client
            val updatedClient = Client(
                id = client.id,
                name = client.name,
                document = client.document,
                email = client.email,
                password = client.password,
                membershipType = client.membershipType,
                amount = client.amount,
                status = if (hasValidAptitude) PaymentStatus.PAID else client.status,
                hasValidMedicalAptitude = hasValidAptitude,
                expirationDate = client.expirationDate
            )
            users[userIndex] = updatedClient
            return true
        }
        return false
    }

    /**
     * Obtiene todos los clientes del sistema.
     */
    fun getClients(): List<Client> {
        return getUsers().filterIsInstance<Client>()
    }

    /**
     * Obtiene los clientes con pagos vencidos.
     */
    fun getOverdueClients(): List<Client> {
        return getClients().filter { it.status == PaymentStatus.OVERDUE }
    }

    /**
     * Calcula el monto total de los pagos vencidos.
     */
    fun getTotalOverdueAmount(): Double {
        return getClients().filter { it.status == PaymentStatus.OVERDUE }
            .sumOf { user ->
                user.amount.replace(Regex("[^\\d.]"), "").toDoubleOrNull() ?: 0.0
            }
    }
}
