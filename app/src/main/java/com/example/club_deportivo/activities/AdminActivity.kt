package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.ui.CustomFilter
import com.example.club_deportivo.models.PaymentStatus
import com.example.club_deportivo.models.UserRepository
import com.example.club_deportivo.ui.UserAdapter
import com.example.club_deportivo.ui.CustomHeader
import kotlin.math.roundToInt

class AdminActivity : BaseAuthActivity() {
    private lateinit var paidFilter: TextView
    private lateinit var dueSoonFilter: TextView
    private lateinit var overdueFilter: TextView

    private lateinit var userAdapter: UserAdapter
    private val allUsers = UserRepository.getClients()

    private var currentSearchQuery: String = ""
    private var activeStatusFilters: Set<PaymentStatus> = setOf(PaymentStatus.PAID, PaymentStatus.DUE_SOON, PaymentStatus.OVERDUE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        paidFilter = findViewById(R.id.paid_filter)
        dueSoonFilter = findViewById(R.id.due_soon_filter)
        overdueFilter = findViewById(R.id.overdue_filter)

        setupHeader()
        setupSummaryCard()
        setupRecyclerView()
        setupFilterManager()
        setupSearch()
        setupAddUserButton()
    }

    /**
     * Configura el RecyclerView, su adaptador y la lista inicial de usuarios.
     */
    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        userAdapter = UserAdapter(allUsers) { userName ->
            handlePayment(userName)
        }
        recyclerView.adapter = userAdapter
        updateUserListVisibility()
    }

    /**
     * Configura el listener para el botón de añadir usuario, que inicia la RegisterActivity.
     */
    private fun setupAddUserButton() {
        val addUserButton = findViewById<View>(R.id.addUserCard)
        addUserButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Configura el header personalizado.
     */
    private fun setupHeader() {
        CustomHeader.setupAdminHeader(this)
    }

    /**
     * Calcula y muestra los datos en la tarjeta de resumen (total y cantidad de deudores).
     */
    private fun setupSummaryCard() {
        val summaryLabel = findViewById<TextView>(R.id.summaryLabel)
        val summaryAmount = findViewById<TextView>(R.id.summaryAmount)
        val summaryDetail = findViewById<TextView>(R.id.summaryDetail)

        summaryLabel.text = getString(R.string.overdue)

        val overdueUsers = UserRepository.getOverdueClients()

        val totalOverdueAmount = UserRepository.getTotalOverdueAmount()
        summaryAmount.text = getString(R.string.currency_format, totalOverdueAmount.roundToInt())


        val userCount = overdueUsers.size
        summaryDetail.text = resources.getQuantityString(R.plurals.user_count, userCount, userCount)
    }

    /**
     * Inicializa el gestor de filtros de estado (Pagado, Por Vencer, Vencido).
     */
    private fun setupFilterManager() {
        CustomFilter(
            context = this,
            paidFilter = paidFilter,
            dueSoonFilter = dueSoonFilter,
            overdueFilter = overdueFilter
        ) { activeFilters ->
            activeStatusFilters = activeFilters
            updateUserListVisibility()
        }
    }

    /**
     * Configura el listener del campo de búsqueda para filtrar la lista en tiempo real.
     */
    private fun setupSearch() {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener { text ->
            currentSearchQuery = text.toString()
            updateUserListVisibility()
        }
    }

    /**
     * Aplica los filtros de búsqueda por texto y estado a la lista de usuarios y actualiza el adaptador.
     */
    private fun updateUserListVisibility() {
        val searchResults = if (currentSearchQuery.isBlank()) {
            allUsers
        } else {
            allUsers.filter { it.name.contains(currentSearchQuery, ignoreCase = true) }
        }

        val finalFilteredUsers = searchResults.filter { activeStatusFilters.contains(it.status) }

        userAdapter.updateUsers(finalFilteredUsers)
    }

    /**
     * Maneja el evento de clic en el botón de pago de un usuario.
     */
    private fun handlePayment(userName: String) {
        // TODO: Implementar lógica de pago (ej. mostrar un diálogo de confirmación)
        println("Procesar pago para: $userName")
    }
}