package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.google.android.material.card.MaterialCardView

enum class ActionCardStyle {
    PRIMARY,
    SECONDARY,
    SUCCESS,
    ERROR
}

object CustomActionCard {
    fun setup(
        card: MaterialCardView,
        iconResId: Int,
        title: String,
        subtitle: String,
        style: ActionCardStyle
    ) {
        val context = card.context

        val icon = card.findViewById<ImageView>(R.id.action_icon)
        val titleView = card.findViewById<TextView>(R.id.action_title)
        val subtitleView = card.findViewById<TextView>(R.id.action_subtitle)
        val backgroundView = card.findViewById<View>(R.id.action_icon_background)

        icon.setImageResource(iconResId)
        titleView.text = title
        subtitleView.text = subtitle

        when (style) {
            ActionCardStyle.PRIMARY -> {
                backgroundView.background = ContextCompat.getDrawable(context, R.drawable.circle_background_primary_light)
                icon.imageTintList = ContextCompat.getColorStateList(context, R.color.primary_main)
            }
            ActionCardStyle.SECONDARY -> {
                backgroundView.background = ContextCompat.getDrawable(context, R.drawable.circle_background_secondary_light)
                icon.imageTintList = ContextCompat.getColorStateList(context, R.color.secondary_main)
            }
            ActionCardStyle.SUCCESS -> {
                backgroundView.background = ContextCompat.getDrawable(context, R.drawable.circle_background_success_light)
                icon.imageTintList = ContextCompat.getColorStateList(context, R.color.success_main)
            }
            ActionCardStyle.ERROR -> {
                backgroundView.background = ContextCompat.getDrawable(context, R.drawable.circle_background_error_light)
                icon.imageTintList = ContextCompat.getColorStateList(context, R.color.error_main)
            }
        }
    }
}
