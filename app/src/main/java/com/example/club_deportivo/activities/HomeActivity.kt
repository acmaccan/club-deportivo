package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.ActivityDatabaseRepository
import com.example.club_deportivo.models.Client
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.models.MembershipType
import com.example.club_deportivo.ui.CustomHeader
import com.google.android.material.card.MaterialCardView
import com.example.club_deportivo.models.PaymentStatus
import com.example.club_deportivo.ui.ActionCardStyle
import com.example.club_deportivo.ui.ActivityAdapter
import com.example.club_deportivo.ui.CustomActionCard
import com.example.club_deportivo.ui.CustomHomeMembershipStatusCard
import com.example.club_deportivo.ui.UpcomingActivityAdapter

class HomeActivity : BaseAuthActivity() {
    private lateinit var activityRepository: ActivityDatabaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        activityRepository = ActivityDatabaseRepository(this)

        val clientUser = user as? Client
        if (clientUser == null) {
            finish()
            return
        }

        CustomHeader.setupHomeHeader(this, clientUser.name) {
            navigateToProfile()
        }
        setupMembershipCard(clientUser)
        setupActionCards(clientUser)
        setupActivitiesSection()
        setupSchedulesSection()
    }

    /**
     * Configura la tarjeta de estado de la membresía (activa o vencida).
     */
    private fun setupMembershipCard(user: Client) {
        val membershipStatus = if (user.status == PaymentStatus.OVERDUE || !user.hasValidMedicalAptitude) {
            MembershipStatus.DISABLED
        } else {
            MembershipStatus.ENABLED
        }

        val membershipCardView = findViewById<MaterialCardView>(R.id.membershipStatusCard)
        CustomHomeMembershipStatusCard.setup(membershipCardView, membershipStatus, user.membershipType)
        membershipCardView.setOnClickListener {
            navigateToProfile()
        }
    }

    /**
     * Navegación al perfil.
     */
    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, user.id)
        }
        startActivity(intent)
    }

    /**
     * Navegación a pagos.
     */
    private fun navigateToPayments() {
        val intent = Intent(this, PaymentsActivity::class.java).apply {
            putExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, user.id)
        }
        startActivity(intent)
    }

    /**
     * Configura las tres tarjetas de acción (Apto Médico, Actividades, Pagos).
     */
    private fun setupActionCards(user: Client) {
        val cardMedical = findViewById<MaterialCardView>(R.id.card_action_medical)

        if (user.hasValidMedicalAptitude) {
            CustomActionCard.setup(
                card = cardMedical,
                iconResId = R.drawable.icon_check,
                title = getString(R.string.home_actions_medical_aptitude),
                subtitle = getString(R.string.home_actions_valid_medical_aptitude),
                style = ActionCardStyle.SUCCESS
            )
        } else {
            CustomActionCard.setup(
                card = cardMedical,
                iconResId = R.drawable.icon_x,
                title = getString(R.string.home_actions_medical_aptitude),
                subtitle = getString(R.string.home_actions_invalid_medical_aptitude),
                style = ActionCardStyle.ERROR
            )
        }

        val cardActivities = findViewById<MaterialCardView>(R.id.card_action_activities)
        CustomActionCard.setup(
            card = cardActivities,
            iconResId = R.drawable.icon_person,
            title = getString(R.string.home_activities),
            subtitle = getString(R.string.home_actions_see_more),
            style = ActionCardStyle.SECONDARY
        )

        val cardPayment = findViewById<MaterialCardView>(R.id.card_action_payment)
        val paymentSubtitle = if (user.membershipType == MembershipType.NO_MEMBER) {
            getString(R.string.home_actions_activities_payment)
        } else {
            getString(R.string.home_actions_monthly_payment)
        }
        CustomActionCard.setup(
            card = cardPayment,
            iconResId = R.drawable.icon_payment_white,
            title = getString(R.string.payments),
            subtitle = paymentSubtitle,
            style = ActionCardStyle.PRIMARY
        )
        cardPayment.setOnClickListener {
            navigateToPayments()
        }
    }

    /**
     * Configura el RecyclerView para la lista horizontal de actividades.
     */
    private fun setupActivitiesSection() {
        val recyclerViewActivities = findViewById<RecyclerView>(R.id.recycler_view_activities)
        val activities = activityRepository.getActivitiesForUI()
        val activityAdapter = ActivityAdapter(activities)

        recyclerViewActivities.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL, false
            )
        recyclerViewActivities.adapter = activityAdapter

    }

    /**
     * Configura el RecyclerView para la lista horizontal de próximos horarios.
     */
    private fun setupSchedulesSection() {
        val recyclerViewSchedules = findViewById<RecyclerView>(R.id.recycler_view_schedules)
        val emptyMessage = findViewById<android.widget.TextView>(R.id.empty_schedules_message)

        val upcomingActivitiesWithStatus = activityRepository.getUserEnrolledActivitiesWithStatus(user.id)

        if (upcomingActivitiesWithStatus.isEmpty()) {
            recyclerViewSchedules.visibility = android.view.View.GONE
            emptyMessage.visibility = android.view.View.VISIBLE
        } else {
            recyclerViewSchedules.visibility = android.view.View.VISIBLE
            emptyMessage.visibility = android.view.View.GONE

            val scheduleAdapter = UpcomingActivityAdapter(upcomingActivitiesWithStatus)
            recyclerViewSchedules.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewSchedules.adapter = scheduleAdapter
        }
    }
}