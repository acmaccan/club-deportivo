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
    companion object {
        const val PAYMENT_RESUME_ITEM_TITLE = "ITEM_TITLE"
        const val PAYMENT_RESUME_ITEM_SUBTITLE = "ITEM_SUBTITLE"
        const val PAYMENT_RESUME_ITEM_SCHEDULE = "ITEM_SCHEDULE"
        const val PAYMENT_RESUME_ITEM_PRICE = "ITEM_PRICE"
        const val PAYMENT_RESUME_SUCCESS = "PAYMENT_SUCCESS"
        const val LOGGED_USER_ID_KEY = "LOGGED_USER_ID_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_resume)

        val isSuccess = intent.getBooleanExtra(PAYMENT_RESUME_SUCCESS, false)
        val itemTitle = intent.getStringExtra(PAYMENT_RESUME_ITEM_TITLE) ?: "No disponible"
        val itemSubtitle = intent.getStringExtra(PAYMENT_RESUME_ITEM_SUBTITLE) ?: "No disponible"
        val itemSchedule = intent.getStringExtra(PAYMENT_RESUME_ITEM_SCHEDULE) ?: ""
        val itemPrice = intent.getStringExtra(PAYMENT_RESUME_ITEM_PRICE) ?: "$0"

        setupUI(isSuccess)
        setupSummaryCard(isSuccess, itemTitle, itemSubtitle, itemSchedule, itemPrice)
        setupListeners(isSuccess)
    }

    private fun setupUI(isSuccess: Boolean) {
        val resumeIcon: ImageView = findViewById(R.id.resume_icon)
        val resumeTitle: TextView = findViewById(R.id.payment_resume_title)
        val resumeSubtitle: TextView = findViewById(R.id.payment_resume_subtitle)
        val primaryButton: CustomButton = findViewById(R.id.primary_button)
        val secondaryButton: CustomButton = findViewById(R.id.secondary_button)

        if (isSuccess) {
            resumeIcon.setImageResource(R.drawable.icon_check)
            resumeIcon.background.setTint(ContextCompat.getColor(this, R.color.success_main))
            resumeTitle.text = getString(R.string.payment_resume_success_title)
            resumeSubtitle.text = getString(R.string.payment_resume_success_subtitle)
            primaryButton.setText(getString(R.string.payment_resume_back_to_home))
            secondaryButton.visibility = View.GONE
        } else {
            resumeIcon.setImageResource(R.drawable.icon_x)
            resumeIcon.background.setTint(ContextCompat.getColor(this, R.color.error_main))
            resumeTitle.text = getString(R.string.payment_resume_error_title)
            resumeSubtitle.text = getString(R.string.payment_resume_error_subtitle)
            primaryButton.setText(getString(R.string.payment_resume_retry))
            secondaryButton.setText(getString(R.string.payment_resume_back_to_home))
            secondaryButton.visibility = View.VISIBLE
        }
    }

    private fun setupSummaryCard(
        isSuccess: Boolean,
        title: String,
        subtitle: String,
        schedule: String,
        price: String
    ) {
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

        itemTitle.text = title
        itemSubtitle.text = subtitle
        itemSchedule.text = schedule
        itemPrice.text = price
    }

    private fun setupListeners(isSuccess: Boolean) {
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