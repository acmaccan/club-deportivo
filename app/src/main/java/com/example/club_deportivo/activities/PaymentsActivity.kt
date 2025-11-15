package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.ActivityDatabaseRepository
import com.example.club_deportivo.models.Client
import com.example.club_deportivo.models.MembershipType
import com.example.club_deportivo.models.PaymentRepository
import com.example.club_deportivo.ui.CustomButton
import com.example.club_deportivo.ui.PaymentActivityAdapter
import com.google.android.material.card.MaterialCardView

class PaymentsActivity : BaseAuthActivity() {

    private lateinit var monthlyPaymentCard: MaterialCardView
    private lateinit var activitySelectionSection: View
    private lateinit var totalAmountText: TextView
    private lateinit var paymentTypeLabel: TextView
    private lateinit var paymentButton: CustomButton
    private lateinit var activitiesRecyclerView: RecyclerView
    private var activityAdapter: PaymentActivityAdapter? = null
    private lateinit var activityRepository: ActivityDatabaseRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        activityRepository = ActivityDatabaseRepository(this)

        val clientUser = user as? Client
        if (clientUser == null) {
            finish()
            return
        }

        initViews()
        setupToolbar()
        setupPaymentScreen(clientUser)
    }
    
    private fun initViews() {
        monthlyPaymentCard = findViewById(R.id.monthlyPaymentCard)
        activitySelectionSection = findViewById(R.id.activitySelectionSection)
        totalAmountText = findViewById(R.id.totalAmountText)
        paymentTypeLabel = findViewById(R.id.paymentTypeLabel)
        paymentButton = findViewById(R.id.paymentButton)
        activitiesRecyclerView = findViewById(R.id.activitiesRecyclerView)
    }
    
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    
    private fun setupPaymentScreen(user: Client) {
        when (user.membershipType) {
            MembershipType.MEMBER -> setupMonthlyPayment()
            MembershipType.NO_MEMBER -> setupActivitySelection()
        }
    }
    
    private fun setupMonthlyPayment() {
        monthlyPaymentCard.visibility = View.VISIBLE
        activitySelectionSection.visibility = View.GONE
        
        val monthlyPayment = PaymentRepository.getMonthlyPayment()
        
        findViewById<TextView>(R.id.monthlyPaymentTitle).text = 
            getString(R.string.payments_monthly_fee_format, monthlyPayment.month, monthlyPayment.year)
        findViewById<TextView>(R.id.monthlyPaymentDue).text = 
            getString(R.string.payments_expires_in_format, monthlyPayment.daysUntilDue)
        findViewById<TextView>(R.id.monthlyPaymentAmount).text = 
            getString(R.string.payments_amount_format, monthlyPayment.amount)
        findViewById<TextView>(R.id.monthlyPaymentDescription).text = 
            monthlyPayment.description
        
        totalAmountText.text = getString(R.string.payments_amount_format, monthlyPayment.amount)
        paymentTypeLabel.text = getString(R.string.payments_monthly_fee_label)
        paymentButton.text = getString(R.string.payments_pay_monthly_fee)
        
        paymentButton.setOnClickListener {
            navigateToPaymentResume(
                paymentTitle = getString(R.string.payments_monthly_fee_format, monthlyPayment.month, monthlyPayment.year),
                paymentSubtitle = monthlyPayment.description,
                paymentSchedule = "",
                paymentPrice = getString(R.string.payments_amount_format, monthlyPayment.amount)
            )
        }
    }
    
    private fun setupActivitySelection() {
        monthlyPaymentCard.visibility = View.GONE
        activitySelectionSection.visibility = View.VISIBLE

        val activities = activityRepository.getActivities()
        activityAdapter = PaymentActivityAdapter(activities) { selectedActivity ->
            updateTotalAmount(selectedActivity?.monthlyPrice ?: 0)
            paymentButton.isEnabled = selectedActivity != null
        }

        activitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        activitiesRecyclerView.adapter = activityAdapter

        paymentTypeLabel.text = getString(R.string.payments_activity_fee_label)
        paymentButton.text = getString(R.string.payments_pay_selected_activity)
        paymentButton.isEnabled = false
        updateTotalAmount(0)

        paymentButton.setOnClickListener {
            val selectedActivity = activityAdapter?.getSelectedActivity()
            if (selectedActivity != null) {
                navigateToPaymentResume(
                    paymentTitle = selectedActivity.name,
                    paymentSubtitle = selectedActivity.instructor,
                    paymentSchedule = selectedActivity.schedule,
                    paymentPrice = getString(R.string.payments_amount_format, selectedActivity.monthlyPrice)
                )
            }
        }
    }
    
    private fun updateTotalAmount(amount: Int) {
        totalAmountText.text = getString(R.string.payments_amount_format, amount)
    }
    
    private fun navigateToPaymentResume(
        paymentTitle: String,
        paymentSubtitle: String,
        paymentSchedule: String,
        paymentPrice: String
    ) {
        println("PaymentsActivity - Navigating to PaymentResumeActivity with params:")
        println("  PAYMENT_TITLE: $paymentTitle")
        println("  PAYMENT_SUBTITLE: $paymentSubtitle")
        println("  PAYMENT_SCHEDULE: $paymentSchedule")
        println("  PAYMENT_PRICE: $paymentPrice")
        println("  PAYMENT_SUCCESS: true")
        println("  LOGGED_USER_ID_KEY: ${user.id}")

        val intent = Intent(this, PaymentResumeActivity::class.java).apply {
            putExtra(PaymentResumeActivity.PAYMENT_RESUME_ITEM_TITLE, paymentTitle)
            putExtra(PaymentResumeActivity.PAYMENT_RESUME_ITEM_SUBTITLE, paymentSubtitle)
            putExtra(PaymentResumeActivity.PAYMENT_RESUME_ITEM_SCHEDULE, paymentSchedule)
            putExtra(PaymentResumeActivity.PAYMENT_RESUME_ITEM_PRICE, paymentPrice)
            putExtra(PaymentResumeActivity.PAYMENT_RESUME_SUCCESS, true)
            putExtra(PaymentResumeActivity.LOGGED_USER_ID_KEY, user.id)

            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        startActivity(intent)
    }
}