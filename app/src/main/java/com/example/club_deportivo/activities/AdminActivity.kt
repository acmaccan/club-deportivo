package com.example.club_deportivo.activities

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.example.club_deportivo.ui.FilterManager
import com.example.club_deportivo.ui.TagStatus
import com.example.club_deportivo.models.UserData
import com.example.club_deportivo.ui.UserCardHelper

class AdminActivity : AppCompatActivity() {

    private lateinit var paidFilter: TextView
    private lateinit var dueSoonFilter: TextView
    private lateinit var overdueFilter: TextView

    private val userList = listOf(
        UserData(1, R.id.userCard1, "Juan Pérez", "+54 11 5555-1234", "Pase Libre", "$15000", TagStatus.PAID),
        UserData(2, R.id.userCard2, "María Gómez", "+54 11 5555-5678", "Actividades", "$12000", TagStatus.DUE_SOON),
        UserData(3, R.id.userCard3, "Carlos Ruiz", "+54 11 5555-9012", "Pase Libre", "$35000", TagStatus.OVERDUE, showPayButton = true)
    )

    private var currentSearchQuery: String = ""
    private var activeStatusFilters: Set<TagStatus> = setOf(TagStatus.PAID, TagStatus.DUE_SOON, TagStatus.OVERDUE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        paidFilter = findViewById(R.id.paid_filter)
        dueSoonFilter = findViewById(R.id.due_soon_filter)
        overdueFilter = findViewById(R.id.overdue_filter)

        setupHeader()
        setupSummaryCard()
        setupUserCards()
        setupFilterManager()
        setupSearch()
    }

    private fun setupHeader() {
        val headerView = findViewById<android.view.View>(R.id.admin_header)
        val subtitleTextView = headerView.findViewById<TextView>(R.id.headerSubtitle)
        subtitleTextView.text = getString(R.string.basic_management)
    }

    private fun setupSummaryCard() {
        val summaryLabel = findViewById<TextView>(R.id.summaryLabel)
        val summaryAmount = findViewById<TextView>(R.id.summaryAmount)
        val summaryDetail = findViewById<TextView>(R.id.summaryDetail)

        summaryLabel.text = "Pagos vencidos"
        summaryAmount.text = "$35000"
        summaryDetail.text = "1 usuario"
    }

    private fun setupFilterManager() {
        FilterManager(
            context = this,
            paidFilter = paidFilter,
            dueSoonFilter = dueSoonFilter,
            overdueFilter = overdueFilter
        ) { activeFilters ->
            activeStatusFilters = activeFilters
            updateUserListVisibility()
        }
    }

    private fun setupUserCards() {
        userList.forEach { user ->
            val cardView = findViewById<MaterialCardView>(user.cardId)
            UserCardHelper.setup(cardView, user) { userName ->
                handlePayment(userName)
            }
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
            userList
        } else {
            userList.filter { it.name.contains(currentSearchQuery, ignoreCase = true) }
        }

        val finalFilteredUsers = searchResults.filter { activeStatusFilters.contains(it.status) }

        userList.forEach { user ->
            val userCard = findViewById<View>(user.cardId)
            userCard.visibility = if (finalFilteredUsers.contains(user)) View.VISIBLE else View.GONE
        }
    }

    private fun handlePayment(userName: String) {
        // TODO: Implementar lógica de pago (ej. mostrar un diálogo de confirmación)
        println("Procesar pago para: $userName")
    }
}