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
        elevation = 0f

        strokeWidth = (1 * resources.displayMetrics.density).toInt()

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
        val membershipStatus = when (client.status) {
            PaymentStatus.PAID, PaymentStatus.DUE_SOON -> MembershipStatus.ENABLED
            PaymentStatus.OVERDUE -> MembershipStatus.DISABLED
        }

        if (membershipStatus == MembershipStatus.DISABLED) {
            setupDisabledState()
        } else {
            setupEnabledState(client)
        }
    }

    private fun setupDisabledState() {
        val errorColor = ContextCompat.getColor(context, R.color.error_main)
        val errorBgColor = ContextCompat.getColor(context, R.color.error_light)

        setCardBackgroundColor(errorBgColor)
        strokeColor = errorColor
        statusTitle.text = context.getString(MembershipStatus.DISABLED.title)
        statusTitle.setTextColor(errorColor)
        statusIcon.setImageResource(R.drawable.icon_x)
        statusIcon.background.setTint(errorColor)

        planDetailsText.visibility = View.GONE
        benefitsContainer.visibility = View.GONE
        benefitsTitle.visibility = View.GONE
        expirationTitle.visibility = View.GONE
        expirationDateText.visibility = View.GONE
    }

    private fun setupEnabledState(client: Client) {
        val membershipType = client.membershipType
        val primaryColor = ContextCompat.getColor(context, membershipType.titleColor)
        val secondaryColor = ContextCompat.getColor(context, membershipType.descriptionColor)
        val backgroundColor = ContextCompat.getColor(context, membershipType.cardBackgroundColor)

        setCardBackgroundColor(backgroundColor)
        strokeColor = primaryColor
        statusTitle.setTextColor(primaryColor)
        planNameText.setTextColor(primaryColor)
        planDetailsText.setTextColor(secondaryColor)
        benefitsTitle.setTextColor(primaryColor)
        expirationTitle.setTextColor(secondaryColor)
        expirationDateText.setTextColor(primaryColor)
        statusIcon.setImageResource(R.drawable.icon_check)
        statusIcon.background.setTint(ContextCompat.getColor(context, R.color.success_main))

        statusTitle.text = context.getString(MembershipStatus.ENABLED.title)
        planNameText.text = context.getString(membershipType.textTitle)
        planDetailsText.text = context.getString(membershipType.textDescription)
        expirationDateText.text = client.expirationDate
        expirationTitle.text = context.getString(R.string.profile_enabled_membership)

        planDetailsText.visibility = View.VISIBLE
        benefitsContainer.visibility = View.VISIBLE
        benefitsTitle.visibility = View.VISIBLE
        expirationDateText.visibility = View.VISIBLE

        getBenefitsList(client.membershipType.benefits, secondaryColor)
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
