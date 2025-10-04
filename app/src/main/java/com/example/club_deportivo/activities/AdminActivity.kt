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

class AdminActivity : AppCompatActivity() {

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

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        userAdapter = UserAdapter(allUsers) { userName ->
            handlePayment(userName)
        }
        recyclerView.adapter = userAdapter
        updateUserListVisibility()
    }

    private fun setupAddUserButton() {
        val addUserButton = findViewById<View>(R.id.addUserCard)
        addUserButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupHeader() {
        CustomHeader.setupAdminHeader(this)
    }

    private fun setupSummaryCard() {
        val summaryLabel = findViewById<TextView>(R.id.summaryLabel)
        val summaryAmount = findViewById<TextView>(R.id.summaryAmount)
        val summaryDetail = findViewById<TextView>(R.id.summaryDetail)

        summaryLabel.text = getString(R.string.overdue)

        val overdueUsers = allUsers.filter { it.status == PaymentStatus.OVERDUE }

        val totalOverdueAmount = overdueUsers.sumOf { user ->
            user.amount.replace(Regex("[^\\d.]"), "").toDoubleOrNull() ?: 0.0
        }

        summaryAmount.text = String.format("$%.0f", totalOverdueAmount)

        val userCount = overdueUsers.size
        summaryDetail.text = "$userCount usuarios"
    }


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

    private fun setupSearch() {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener { text ->
            currentSearchQuery = text.toString()
            updateUserListVisibility()
        }
    }

    private fun updateUserListVisibility() {
        val searchResults = if (currentSearchQuery.isBlank()) {
            allUsers
        } else {
            allUsers.filter { it.name.contains(currentSearchQuery, ignoreCase = true) }
        }

        val finalFilteredUsers = searchResults.filter { activeStatusFilters.contains(it.status) }

        userAdapter.updateUsers(finalFilteredUsers)
    }

    private fun handlePayment(userName: String) {
        // TODO: Implementar lógica de pago (ej. mostrar un diálogo de confirmación)
        println("Procesar pago para: $userName")
    }
}