package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.UserData
import com.google.android.material.card.MaterialCardView

object UserCardHelper {

    fun setup(
        card: MaterialCardView,
        user: UserData,
        onPayButtonClick: (String) -> Unit
    ) {
        val context = card.context

        val userName = card.findViewById<TextView>(R.id.userName)
        val userPhone = card.findViewById<TextView>(R.id.userPhone)
        val userMembership = card.findViewById<TextView>(R.id.userStatus)
        val paymentAmount = card.findViewById<TextView>(R.id.paymentAmount)
        val paymentStatus = card.findViewById<TextView>(R.id.paymentStatus)
        val statusIndicator = card.findViewById<ImageView>(R.id.statusIndicator)
        val payButton = card.findViewById<View>(R.id.payButton)

        userName.text = user.name
        userPhone.text = user.phone
        userMembership.text = user.membership
        paymentAmount.text = user.amount

        when (user.status) {
            TagStatus.PAID -> {
                paymentStatus.text = "Al día"
                paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.success_dark))
                statusIndicator.setImageResource(R.drawable.icon_smile)
            }
            TagStatus.DUE_SOON -> {
                paymentStatus.text = "Por vencer"
                paymentStatus.setTextColor(ContextCompat.getColor(context, R.color.warning_dark))
                statusIndicator.setImageResource(R.drawable.icon_clock)
            }
            TagStatus.OVERDUE -> {
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
