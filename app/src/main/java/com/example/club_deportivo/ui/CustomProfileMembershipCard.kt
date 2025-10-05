package com.example.club_deportivo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.Client
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.models.PaymentStatus
import com.google.android.material.card.MaterialCardView

class CustomProfileMembershipCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val statusTitle: TextView
    private val planNameText: TextView
    private val planDetailsText: TextView
    private val statusIcon: ImageView
    private val benefitsContainer: LinearLayout
    private val expirationDateText: TextView
    private val expirationTitle: TextView
    private val benefitsTitle: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.component_profile_membership_card, this, true)
        strokeWidth = (1 * resources.displayMetrics.density).toInt()
        radius = 16 * resources.displayMetrics.density

        statusTitle = findViewById(R.id.status_title)
        planNameText = findViewById(R.id.plan_name_text)
        planDetailsText = findViewById(R.id.plan_details_text)
        statusIcon = findViewById(R.id.status_icon)
        benefitsContainer = findViewById(R.id.benefits_container)
        expirationDateText = findViewById(R.id.expiration_date_text)
        expirationTitle = findViewById(R.id.expiration_title)
        benefitsTitle = findViewById(R.id.benefits_title)
    }

    fun setup(client: Client) {
        val status = when (client.status) {
            PaymentStatus.PAID, PaymentStatus.DUE_SOON -> MembershipStatus.ENABLED
            PaymentStatus.OVERDUE -> MembershipStatus.DISABLED
        }

        val type = client.membershipType

        statusTitle.text = context.getString(status.title)
        statusIcon.setImageResource(status.icon)
        statusIcon.setBackgroundResource(status.iconBackground)

        if (status == MembershipStatus.DISABLED) {
            val errorColor = ContextCompat.getColor(context, R.color.error_main)

            setCardBackgroundColor(ContextCompat.getColor(context, R.color.error_light))
            strokeColor = errorColor
            statusTitle.setTextColor(errorColor)
            expirationTitle.setTextColor(errorColor)
            expirationTitle.text = context.getString(R.string.profile_disabled_membership)

            planNameText.visibility = View.GONE
            planDetailsText.visibility = View.GONE
            benefitsTitle.visibility = View.GONE
            benefitsContainer.visibility = View.GONE
            expirationDateText.visibility = View.GONE

        } else {
            val primaryColor = ContextCompat.getColor(context, type.titleColor)
            val secondaryColor = ContextCompat.getColor(context, type.descriptionColor)

            setCardBackgroundColor(ContextCompat.getColor(context, type.cardBackgroundColor))
            strokeColor = primaryColor
            statusTitle.setTextColor(primaryColor)

            planNameText.text = context.getString(type.textTitle)
            planNameText.setTextColor(primaryColor)

            planDetailsText.text = context.getString(type.textDescription)
            planDetailsText.setTextColor(secondaryColor)

            benefitsTitle.text = context.getString(R.string.profile_benefit_item_format, context.getString(type.textTitle))
            benefitsTitle.setTextColor(primaryColor)

            expirationTitle.text = context.getString(R.string.profile_enabled_membership)
            expirationTitle.setTextColor(secondaryColor)

            expirationDateText.text = client.expirationDate
            expirationDateText.setTextColor(primaryColor)

            getBenefitsList(type.benefits, secondaryColor)

            planNameText.visibility = View.VISIBLE
            planDetailsText.visibility = View.VISIBLE
            benefitsTitle.visibility = View.VISIBLE
            benefitsContainer.visibility = View.VISIBLE
            expirationDateText.visibility = View.VISIBLE
            expirationTitle.visibility = View.VISIBLE
        }
    }

    private fun getBenefitsList(benefits: List<Int>, textColor: Int) {
        benefitsContainer.removeAllViews()
        benefits.forEach { benefitResId ->
            val benefitText = context.getString(benefitResId)
            val benefitTextView = TextView(context).apply {
                text = context.getString(R.string.profile_benefit_item_format, benefitText)
                setTextColor(textColor)
                textSize = 14f
                setPadding(0, 2, 0, 2)
            }
            benefitsContainer.addView(benefitTextView)
        }
    }
}
