package com.example.club_deportivo.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.models.MembershipType
import com.example.club_deportivo.ui.CustomHeader
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userName = "Kevin Del Bello"
        CustomHeader.setupHomeHeader(this, userName)

        val userStatus = MembershipStatus.ENABLED
        val userMembership = MembershipType.MEMBER

        val membershipCardView = findViewById<MaterialCardView>(R.id.membershipStatusCard)

        setupMembershipCard(membershipCardView, userStatus, userMembership)
    }

    private fun setupMembershipCard(
        cardView: MaterialCardView,
        status: MembershipStatus,
        type: MembershipType
    ) {
        val statusIcon = cardView.findViewById<ImageView>(R.id.statusIcon)
        val cardTitle = cardView.findViewById<TextView>(R.id.cardTitle)
        val membershipTag = cardView.findViewById<TextView>(R.id.membershipTag)
        val membershipDescription = cardView.findViewById<TextView>(R.id.membershipDescription)

        if (status == MembershipStatus.DISABLED) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.error_light))
            statusIcon.setImageResource(R.drawable.icon_x)
            cardTitle.text = "Carnet Inhabilitado"
            cardTitle.setTextColor(ContextCompat.getColor(this, R.color.error_main))
            cardView.isClickable = false
            cardView.findViewById<View>(R.id.membershipTagContainer).visibility = View.GONE

        } else {
            cardView.isClickable = true
            statusIcon.setImageResource(R.drawable.icon_check)
            cardTitle.text = "Carnet Habilitado"
            cardView.findViewById<View>(R.id.membershipTagContainer).visibility = View.VISIBLE

            when (type) {
                MembershipType.MEMBER -> {
                    cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary_light))
                    cardTitle.setTextColor(ContextCompat.getColor(this, R.color.primary_main))
                    membershipTag.text = type.displayName
                    membershipDescription.text = type.description
                }
                MembershipType.NO_MEMBER -> {
                    cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.secondary_light))
                    cardTitle.setTextColor(ContextCompat.getColor(this, R.color.secondary_main))
                    membershipTag.text = type.displayName
                    membershipDescription.text = type.description
                }
            }
        }
    }
}
