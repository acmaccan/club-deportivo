package com.example.club_deportivo.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.ActivityRepository
import com.example.club_deportivo.models.Client
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.ui.CustomHeader
import com.google.android.material.card.MaterialCardView
import com.example.club_deportivo.models.PaymentStatus
import com.example.club_deportivo.ui.ActionCardStyle
import com.example.club_deportivo.ui.ActivityAdapter
import com.example.club_deportivo.ui.CustomActionCard
import com.example.club_deportivo.ui.CustomCardMembership

class HomeActivity : BaseAuthActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val clientUser = user as? Client
        if (clientUser == null) {
            finish()
            return
        }

        CustomHeader.setupHomeHeader(this, clientUser.name)
        setupMembershipCard(clientUser)
        setupActionCards(clientUser)
        setupActivitiesSection()
    }

    /**
     * Configura la tarjeta de estado de la membresía (activa o vencida).
     */
    private fun setupMembershipCard(user: Client) {
        val membershipStatus = if (user.status == PaymentStatus.OVERDUE) {
            MembershipStatus.DISABLED
        } else {
            MembershipStatus.ENABLED
        }

        val membershipCardView = findViewById<MaterialCardView>(R.id.membershipStatusCard)
        CustomCardMembership.setup(membershipCardView, membershipStatus, user.membershipType)
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
                title = getString(R.string.actions_valid_medical_aptitude),
                subtitle = getString(R.string.actions_valid_medical_aptitude),
                style = ActionCardStyle.SUCCESS
            )
        } else {
            CustomActionCard.setup(
                card = cardMedical,
                iconResId = R.drawable.icon_x,
                title = getString(R.string.actions_valid_medical_aptitude),
                subtitle = getString(R.string.actions_valid_medical_aptitude),
                style = ActionCardStyle.ERROR
            )
        }

        val cardActivities = findViewById<MaterialCardView>(R.id.card_action_activities)
        CustomActionCard.setup(
            card = cardActivities,
            iconResId = R.drawable.icon_person,
            title = getString(R.string.actions_activities),
            subtitle = getString(R.string.actions_see_more),
            style = ActionCardStyle.SECONDARY
        )

        val cardPayment = findViewById<MaterialCardView>(R.id.card_action_payment)
        CustomActionCard.setup(
            card = cardPayment,
            iconResId = R.drawable.icon_wallet,
            title = getString(R.string.payments),
            subtitle = getString(R.string.actions_monthly_payment),
            style = ActionCardStyle.PRIMARY
        )
    }

    /**
     * Configura el RecyclerView para la lista horizontal de actividades.
     */
    private fun setupActivitiesSection() {
        val recyclerViewActivities = findViewById<RecyclerView>(R.id.recycler_view_activities)
        val activities = ActivityRepository.getActivities()
        val activityAdapter = ActivityAdapter(activities)

        recyclerViewActivities.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL, false
            )
        recyclerViewActivities.adapter = activityAdapter

    }
}
