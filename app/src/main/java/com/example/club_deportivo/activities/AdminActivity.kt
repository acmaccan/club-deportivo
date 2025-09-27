package com.example.club_deportivo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.example.club_deportivo.ui.StatusTagHelper
import com.example.club_deportivo.ui.TagStatus

class AdminActivity : AppCompatActivity() {

    private lateinit var searchEditText: TextInputEditText
    private lateinit var addUserCard: MaterialCardView

    private lateinit var tagAlDia: TextView
    private lateinit var tagPorVencer: TextView
    private lateinit var tagVencidos: TextView

    private var currentFilter: TagStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        setupHeader()
        setupSummaryCard()
        setupFilters()
        setupUserCards()
        setupSearchAndActions()
    }

    private fun setupHeader() {
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        val headerSubtitle = findViewById<TextView>(R.id.headerSubtitle)

        headerTitle.text = "SportClub"
        headerSubtitle.text = "Gestión básica"
    }

    private fun setupSummaryCard() {
        val summaryLabel = findViewById<TextView>(R.id.summaryLabel)
        val summaryAmount = findViewById<TextView>(R.id.summaryAmount)
        val summaryDetail = findViewById<TextView>(R.id.summaryDetail)

        summaryLabel.text = "Pagos vencidos"
        summaryAmount.text = "$35000"
        summaryDetail.text = "1 usuario"
    }

    private fun setupFilters() {
        tagAlDia = findViewById(R.id.tagAlDia)
        tagPorVencer = findViewById(R.id.tagPorVencer)
        tagVencidos = findViewById(R.id.tagVencidos)

        StatusTagHelper.setupFilterTag(tagAlDia, TagStatus.PAID, this, isSelected = true)
        StatusTagHelper.setupFilterTag(tagPorVencer, TagStatus.DUE_SOON, this, isSelected = false)
        StatusTagHelper.setupFilterTag(tagVencidos, TagStatus.OVERDUE, this, isSelected = false)

        currentFilter = TagStatus.PAID

        tagAlDia.setOnClickListener { toggleFilter(TagStatus.PAID) }
        tagPorVencer.setOnClickListener { toggleFilter(TagStatus.DUE_SOON) }
        tagVencidos.setOnClickListener { toggleFilter(TagStatus.OVERDUE) }
    }

    private fun setupUserCards() {
        setupUserCard(
            R.id.userCard1,
            "Juan Pérez",
            "+ 54 11 5555-1234",
            "Pase Libre",
            "$15000",
            "Al día",
            TagStatus.PAID
        )

        setupUserCard(
            R.id.userCard2,
            "Juan Pérez",
            "+ 54 11 5555-1234",
            "Actividades",
            "$15000",
            "Por vencer",
            TagStatus.DUE_SOON
        )

        setupUserCard(
            R.id.userCard3,
            "Juan Pérez",
            "+ 54 11 5555-1234",
            "Pase Libre",
            "$15000",
            "Vencido",
            TagStatus.OVERDUE,
            showPayButton = true
        )
    }

    private fun setupUserCard(
        cardId: Int,
        name: String,
        phone: String,
        membership: String,
        amount: String,
        statusText: String,
        status: TagStatus,
        showPayButton: Boolean = false
    ) {
        val card = findViewById<LinearLayout>(cardId)

        val userName = card.findViewById<TextView>(R.id.userName)
        val userPhone = card.findViewById<TextView>(R.id.userPhone)
        val userStatus = card.findViewById<TextView>(R.id.userStatus)
        val paymentAmount = card.findViewById<TextView>(R.id.paymentAmount)
        val paymentStatus = card.findViewById<TextView>(R.id.paymentStatus)
        val statusIndicator = card.findViewById<ImageView>(R.id.statusIndicator)

        userName.text = name
        userPhone.text = phone
        userStatus.text = membership
        paymentAmount.text = amount
        paymentStatus.text = statusText

        when (status) {
            TagStatus.PAID -> {
                paymentStatus.setTextColor(getColor(android.R.color.holo_green_dark))
                statusIndicator.setImageResource(R.drawable.icon_smile)
            }
            TagStatus.DUE_SOON -> {
                paymentStatus.setTextColor(getColor(android.R.color.holo_orange_dark))
                statusIndicator.setImageResource(R.drawable.icon_clock)
            }
            TagStatus.OVERDUE -> {
                paymentStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                statusIndicator.setImageResource(R.drawable.icon_x)

                if (showPayButton) {
                    val payButton = card.findViewById<com.google.android.material.button.MaterialButton>(R.id.payButton)
                    payButton.visibility = android.view.View.VISIBLE
                    payButton.setOnClickListener {
                        handlePayment(name)
                    }
                }
            }
        }
    }

    private fun setupSearchAndActions() {
        searchEditText = findViewById(R.id.searchEditText)
        addUserCard = findViewById(R.id.addUserCard)

        addUserCard.setOnClickListener {
            // TODO: Navegar a pantalla de agregar usuario
            println("Agregar usuario")
        }

        // TODO: Implementar búsqueda en tiempo real
    }

    private fun toggleFilter(filterType: TagStatus) {
        // Actualizar estado visual de los filtros
        StatusTagHelper.setupFilterTag(tagAlDia, TagStatus.PAID, this, isSelected = filterType == TagStatus.PAID)
        StatusTagHelper.setupFilterTag(tagPorVencer, TagStatus.DUE_SOON, this, isSelected = filterType == TagStatus.DUE_SOON)
        StatusTagHelper.setupFilterTag(tagVencidos, TagStatus.OVERDUE, this, isSelected = filterType == TagStatus.OVERDUE)

        currentFilter = filterType

        // TODO: Filtrar lista de usuarios según el filtro seleccionado
        println("Filtro activo: $filterType")
    }

    private fun handlePayment(userName: String) {
        // TODO: Implementar lógica de pago
        println("Procesar pago para: $userName")
    }
}