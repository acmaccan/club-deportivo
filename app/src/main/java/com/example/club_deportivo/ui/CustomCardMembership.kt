package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.models.MembershipType
import com.google.android.material.card.MaterialCardView

object CustomCardMembership {

    fun setup(
        cardView: MaterialCardView,
        status: MembershipStatus,
        type: MembershipType
    ) {
        val context = cardView.context

        val statusIcon = cardView.findViewById<ImageView>(R.id.statusIcon)
        val cardTitle = cardView.findViewById<TextView>(R.id.cardTitle)
        val membershipTag = cardView.findViewById<TextView>(R.id.membershipTag)
        val membershipDescription = cardView.findViewById<TextView>(R.id.membershipDescription)

        if (status == MembershipStatus.DISABLED) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.error_light))
            statusIcon.setImageResource(R.drawable.icon_x)
            cardView.findViewById<View>(R.id.statusIconBackground).setBackgroundResource(R.drawable.circle_background_red)
            cardTitle.text = "Carnet Inhabilitado"
            cardTitle.setTextColor(ContextCompat.getColor(context, R.color.error_main))
            cardView.isClickable = false
            cardView.findViewById<View>(R.id.membershipTagContainer).visibility = View.GONE
            cardView.findViewById<ImageView>(R.id.arrowIcon).visibility = View.INVISIBLE
        } else {
            cardView.isClickable = true
            statusIcon.setImageResource(R.drawable.icon_check)
            cardTitle.text = "Carnet Habilitado"
            cardView.findViewById<View>(R.id.membershipTagContainer).visibility = View.VISIBLE
            cardView.findViewById<ImageView>(R.id.arrowIcon).visibility = View.VISIBLE

            when (type) {
                MembershipType.MEMBER -> {
                    cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_light))
                    cardView.findViewById<View>(R.id.statusIconBackground).setBackgroundResource(R.drawable.circle_background_green)
                    cardTitle.setTextColor(ContextCompat.getColor(context, R.color.primary_main))
                    membershipTag.text = type.displayName
                    membershipDescription.text = type.description
                }
                MembershipType.NO_MEMBER -> {
                    cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.secondary_light))
                    cardView.findViewById<View>(R.id.statusIconBackground).setBackgroundResource(R.drawable.circle_background_green)
                    cardTitle.setTextColor(ContextCompat.getColor(context, R.color.secondary_main))
                    membershipTag.text = type.displayName
                    membershipDescription.text = type.description
                }
            }
        }
    }
}
