package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R

class CustomHeader {
    companion object {
        fun setupHeader(
            activity: AppCompatActivity,
            title: String = "SportClub",
            subtitle: String = "",
            showBackButton: Boolean = false,
            showAvatarIcon: Boolean = false,
            onBackClick: (() -> Unit)? = null,
            onAvatarClick: (() -> Unit)? = null
        ) {
            val headerTitle = activity.findViewById<TextView>(R.id.headerTitle)
            val headerSubtitle = activity.findViewById<TextView>(R.id.headerSubtitle)
            val leftIcon = activity.findViewById<ImageView>(R.id.headerLeftIcon)
            val rightIcon = activity.findViewById<ImageView>(R.id.headerRightIcon)

            headerTitle.text = title
            headerSubtitle.text = subtitle

            if (showBackButton) {
                leftIcon.visibility = View.VISIBLE
                leftIcon.setOnClickListener {
                    onBackClick?.invoke() ?: activity.onBackPressedDispatcher.onBackPressed()
                }
            } else {
                leftIcon.visibility = View.GONE
            }

            if (showAvatarIcon) {
                rightIcon.visibility = View.VISIBLE
                rightIcon.setOnClickListener {
                    onAvatarClick?.invoke()
                }
            } else {
                rightIcon.visibility = View.GONE
            }
        }

        fun setupAdminHeader(activity: AppCompatActivity) {
            setupHeader(activity, "SportClub", "@string/basic_management")
        }

        fun setupHomeHeader(
            activity: AppCompatActivity,
            userName: String,
            onAvatarClick: (() -> Unit)? = null
        ) {
            setupHeader(
                activity, "SportClub", "¡Hola, $userName!", showAvatarIcon = true,
                onAvatarClick = onAvatarClick
            )
        }

        fun setupPaymentsHeader(activity: AppCompatActivity, onBackClick: (() -> Unit)? = null) {
            setupHeader(
                activity, "SportClub", "@string/payments", showBackButton = true,
                onBackClick = onBackClick
            )
        }

        fun setupProfileHeader(activity: AppCompatActivity, onBackClick: (() -> Unit)? = null) {
            setupHeader(
                activity, "SportClub", "@string/profile", showBackButton = true,
                onBackClick = onBackClick
            )
        }
    }
}