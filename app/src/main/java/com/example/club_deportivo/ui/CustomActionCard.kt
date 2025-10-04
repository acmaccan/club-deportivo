package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.google.android.material.card.MaterialCardView

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

        backgroundView.background = ContextCompat.getDrawable(context, style.background)
        icon.imageTintList = ContextCompat.getColorStateList(context, style.iconTint)
    }
}
