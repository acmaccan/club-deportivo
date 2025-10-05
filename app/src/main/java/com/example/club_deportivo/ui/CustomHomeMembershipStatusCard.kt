package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.models.MembershipType
import com.google.android.material.card.MaterialCardView

object CustomHomeMembershipStatusCard {
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
        val statusIconBackground = cardView.findViewById<View>(R.id.statusIconBackground)
        val membershipTagContainer = cardView.findViewById<View>(R.id.membershipTagContainer)
        val arrowIcon = cardView.findViewById<ImageView>(R.id.arrowIcon)

        cardTitle.text = context.getString(status.title)
        arrowIcon.visibility = View.VISIBLE

        statusIconBackground.setBackgroundResource(status.iconBackground)
        statusIcon.setImageResource(status.icon)
        statusIcon.imageTintList = ContextCompat.getColorStateList(context, status.iconColor)

        cardView.setCardBackgroundColor(ContextCompat.getColor(context, type.cardBackgroundColor))
        membershipTag.setTextColor(ContextCompat.getColor(context, type.titleColor))
        membershipDescription.setTextColor(ContextCompat.getColor(context, type.descriptionColor))

        if (status == MembershipStatus.DISABLED) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.error_light))
            cardTitle.setTextColor(ContextCompat.getColor(context, R.color.error_main))
            membershipTagContainer.visibility = View.GONE
        } else {
            cardTitle.setTextColor(ContextCompat.getColor(context, type.titleColor))

            membershipTag.text = context.getString(type.textTitle)
            membershipDescription.text = context.getString(type.textDescription)
            membershipTagContainer.visibility = View.VISIBLE
        }
    }
}
