package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.ActivityDatabaseRepository
import com.example.club_deportivo.models.Client
import com.example.club_deportivo.models.PaymentDatabaseRepository
import com.example.club_deportivo.models.PaymentType
import com.example.club_deportivo.models.TransactionStatus
import com.example.club_deportivo.ui.CustomButton
import com.google.android.material.card.MaterialCardView

class PaymentResumeActivity : BaseAuthActivity() {
    companion object {
        const val PAYMENT_RESUME_ITEM_TITLE = "ITEM_TITLE"
        const val PAYMENT_RESUME_ITEM_SUBTITLE = "ITEM_SUBTITLE"
        const val PAYMENT_RESUME_ITEM_SCHEDULE = "ITEM_SCHEDULE"
        const val PAYMENT_RESUME_ITEM_PRICE = "ITEM_PRICE"
        const val PAYMENT_RESUME_SUCCESS = "PAYMENT_SUCCESS"
        const val PAYMENT_RESUME_ACTIVITY_ID = "ACTIVITY_ID"
        const val PAYMENT_RESUME_MONTH = "PAYMENT_MONTH"
        const val PAYMENT_RESUME_YEAR = "PAYMENT_YEAR"
        const val PAYMENT_RESUME_AMOUNT = "PAYMENT_AMOUNT"
        const val PAYMENT_RESUME_DUE_DATE = "PAYMENT_DUE_DATE"
        const val LOGGED_USER_ID_KEY = "LOGGED_USER_ID_KEY"
    }

    private lateinit var activityRepository: ActivityDatabaseRepository
    private lateinit var paymentRepository: PaymentDatabaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_resume)

        activityRepository = ActivityDatabaseRepository(this)
        paymentRepository = PaymentDatabaseRepository(this)

        val clientUser = user as? Client
        if (clientUser == null) {
            finish()
            return
        }

        val isSuccess = intent.getBooleanExtra(PAYMENT_RESUME_SUCCESS, false)
        val itemTitle = intent.getStringExtra(PAYMENT_RESUME_ITEM_TITLE) ?: "No disponible"
        val itemSubtitle = intent.getStringExtra(PAYMENT_RESUME_ITEM_SUBTITLE) ?: "No disponible"
        val itemSchedule = intent.getStringExtra(PAYMENT_RESUME_ITEM_SCHEDULE) ?: ""
        val itemPrice = intent.getStringExtra(PAYMENT_RESUME_ITEM_PRICE) ?: "$0"
        val activityId = intent.getIntExtra(PAYMENT_RESUME_ACTIVITY_ID, -1)
        val paymentMonth = intent.getStringExtra(PAYMENT_RESUME_MONTH) ?: ""
        val paymentYear = intent.getIntExtra(PAYMENT_RESUME_YEAR, 0)
        val paymentAmount = intent.getIntExtra(PAYMENT_RESUME_AMOUNT, 0)
        val paymentDueDate = intent.getStringExtra(PAYMENT_RESUME_DUE_DATE) ?: ""

        println("DEBUG paymentMonth ${paymentMonth}")
        println("DEBUG paymentYear ${paymentYear}")

        if (isSuccess) {
            processPayment(clientUser.id, paymentMonth, paymentYear, paymentAmount, paymentDueDate, activityId)
            processEnrollments(clientUser.id, activityId)
        }

        setupUI(isSuccess)
        setupSummaryCard(isSuccess, itemTitle, itemSubtitle, itemSchedule, itemPrice)
        setupListeners(isSuccess)
    }

    private fun processPayment(
        clientId: Int,
        month: String,
        year: Int,
        amount: Int,
        dueDate: String,
        activityId: Int
    ) {
        val paymentType = if (activityId == -1) PaymentType.MONTHLY_FEE else PaymentType.ACTIVITY_FEE
        val realActivityId = if (activityId == -1) null else activityId

        paymentRepository.createPayment(
            clientId = clientId,
            type = paymentType,
            amount = amount,
            periodMonth = month,
            periodYear = year,
            dueDate = dueDate,
            status = TransactionStatus.SUCCESS,
            activityId = realActivityId
        )
    }

    private fun processEnrollments(userId: Int, activityId: Int) {
        if (activityId == -1) {
            val allActivities = activityRepository.getActivitiesForUI()
            for (activity in allActivities) {
                if (!activityRepository.isUserEnrolled(userId, activity.id)) {
                    activityRepository.enrollUserToActivity(userId, activity.id, "active")
                }
            }
        } else {
            if (!activityRepository.isUserEnrolled(userId, activityId)) {
                activityRepository.enrollUserToActivity(userId, activityId, "active")
            }
        }
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
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, user.id)
            }
            startActivity(intent)
            finish()
        }

        secondaryButton.setOnClickListener {
            finish()
        }
    }
}