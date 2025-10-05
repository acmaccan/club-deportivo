package com.example.club_deportivo.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.ui.CustomButton
import com.google.android.material.card.MaterialCardView

class PaymentResumeActivity : AppCompatActivity() {
    // TODO: Recibir los datos del pago desde un Intent
    private val paymentTitle = "Pilates"
    private val paymentSubtitle = "Ana Rodriguez"
    private val paymentSchedule = "Lu-Mie 18:00"
    private val paymentPrice = "$25.000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_resume)

        // TODO: Recibir el estado real del pago desde un Intent
        val isSuccess = intent.getBooleanExtra("PAYMENT_SUCCESS", true)

        setupUI(isSuccess)
        setupSummaryCard(isSuccess)
        setupListeners()
    }

    private fun setupUI(isSuccess: Boolean) {
        val resumeIcon: ImageView = findViewById(R.id.resume_icon)
        val resumeTitle: TextView = findViewById(R.id.payment_resume_title)
        val resumeSubtitle: TextView = findViewById(R.id.resume_subtitle)
        val primaryButton: CustomButton = findViewById(R.id.primary_button)
        val secondaryButton: CustomButton = findViewById(R.id.secondary_button)

        if (isSuccess) {
            resumeIcon.setImageResource(R.drawable.icon_check)
            resumeTitle.text = getString(R.string.payment_resume_success_title)
            resumeSubtitle.text = getString(R.string.payment_resume_success_subtitle)
            primaryButton.setText(getString(R.string.payment_resume_back_to_home))
            secondaryButton.visibility = View.GONE
        } else {
            resumeIcon.setImageResource(R.drawable.icon_x)
            resumeTitle.text = getString(R.string.payment_resume_error_title)
            resumeSubtitle.text = getString(R.string.payment_resume_error_subtitle)
            primaryButton.setText(getString(R.string.payment_resume_retry))
            secondaryButton.setText(getString(R.string.payment_resume_back_to_home))
            secondaryButton.visibility = View.VISIBLE
        }
    }

    private fun setupSummaryCard(isSuccess: Boolean) {
        val summaryCard: MaterialCardView = findViewById(R.id.payment_resume_card)
        val itemIcon: ImageView = summaryCard.findViewById(R.id.item_icon)
        val itemTitle: TextView = summaryCard.findViewById(R.id.item_title)
        val itemSubtitle: TextView = summaryCard.findViewById(R.id.item_subtitle)
        val itemSchedule: TextView = summaryCard.findViewById(R.id.item_schedule)
        val itemPrice: TextView = summaryCard.findViewById(R.id.item_price)

        val backgroundColor = if (isSuccess) R.color.success_light else R.color.error_light
        val textColor = if (isSuccess) R.color.success_dark else R.color.error_dark
        val iconTintColor = if (isSuccess) R.color.success_main else R.color.error_main

        summaryCard.setCardBackgroundColor(ContextCompat.getColor(this, backgroundColor))
        itemIcon.background.setTint(ContextCompat.getColor(this, backgroundColor))
        itemIcon.setColorFilter(ContextCompat.getColor(this, iconTintColor))

        itemTitle.setTextColor(ContextCompat.getColor(this, textColor))
        itemSubtitle.setTextColor(ContextCompat.getColor(this, textColor))
        itemSchedule.setTextColor(ContextCompat.getColor(this, textColor))
        itemPrice.setTextColor(ContextCompat.getColor(this, textColor))

        itemTitle.text = paymentTitle
        itemSubtitle.text = paymentSubtitle
        itemSchedule.text = paymentSchedule
        itemPrice.text = paymentPrice
    }

    private fun setupListeners() {
        // TODO: Implementar la navegación cuando estén las otras pantallas
        val primaryButton: CustomButton = findViewById(R.id.primary_button)
        val secondaryButton: CustomButton = findViewById(R.id.secondary_button)

        primaryButton.setOnClickListener {
            finish()
        }

        secondaryButton.setOnClickListener {
            finish()
        }
    }
}
