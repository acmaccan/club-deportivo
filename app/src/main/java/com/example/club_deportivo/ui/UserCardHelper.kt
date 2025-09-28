package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.PaymentStatus
import com.example.club_deportivo.models.UserCardData
import com.google.android.material.card.MaterialCardView

object UserCardHelper {

    fun setup(
        card: MaterialCardView,
        user: UserCardData,
        onPayButtonClick: (String) -> Unit
    ) {
        val context = card.context

        val userName = card.findViewById<TextView>(R.id.userName)
        val userEmail = card.findViewById<TextView>(R.id.userEmail)
        val userMembership = card.findViewById<TextView>(R.id.userStatus)
        val paymentAmount = card.findViewById<TextView>(R.id.paymentAmount)
        val paymentStatus = card.findViewById<TextView>(R.id.paymentStatus)
        val statusIndicator = card.findViewById<ImageView>(R.id.statusIndicator)
        val payButton = card.findViewById<View>(R.id.payButton)

        userName.text = user.name
        userEmail.text = user.email
        userMembership.text = user.membershipType.displayName
        paymentAmount.text = user.amount

        when (user.status) {
            PaymentStatus.PAID -> {
                paymentStatus.text = "Al día"
                paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.success_dark))
                statusIndicator.setImageResource(R.drawable.icon_smile)
            }
            PaymentStatus.DUE_SOON -> {
                paymentStatus.text = "Por vencer"
                paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.warning_dark))
                statusIndicator.setImageResource(R.drawable.icon_clock)
            }
            PaymentStatus.OVERDUE -> {
                paymentStatus.text = "Vencido"
                paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.error_dark))
                statusIndicator.setImageResource(R.drawable.icon_x)
            }
        }

        if (user.showPayButton) {
            payButton.visibility = View.VISIBLE
            payButton.setOnClickListener { onPayButtonClick(user.name) }
        } else {
            payButton.visibility = View.GONE
        }
    }
}
