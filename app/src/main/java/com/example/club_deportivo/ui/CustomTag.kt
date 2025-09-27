package com.example.club_deportivo.ui

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R

enum class TagStatus {
    PAID,
    DUE_SOON,
    OVERDUE
}

class StatusTagHelper {
    companion object {
        fun setupTag(
            tagView: TextView,
            status: TagStatus,
            context: Context,
            isEnabled: Boolean = true
        ) {
            when (status) {
                TagStatus.PAID -> {
                    tagView.text = "Al día"
                    tagView.setBackgroundResource(R.drawable.custom_tag_success)
                    tagView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                }

                TagStatus.DUE_SOON -> {
                    tagView.text = "Por vencer"
                    tagView.setBackgroundResource(R.drawable.custom_tag_warning)
                    tagView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                }

                TagStatus.OVERDUE -> {
                    tagView.text = "Vencido"
                    tagView.setBackgroundResource(R.drawable.custom_tag_error)
                    tagView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                }
            }

            tagView.alpha = if (isEnabled) 1.0f else 0.5f
            tagView.isClickable = isEnabled
        }

        fun setupFilterTag(
            tagView: TextView,
            status: TagStatus,
            context: Context,
            isSelected: Boolean
        ) {
            val backgroundDrawableRes: Int
            val darkTextColorRes: Int

            when (status) {
                TagStatus.PAID -> {
                    tagView.text = "Al día"
                    backgroundDrawableRes = R.drawable.custom_tag_success
                    darkTextColorRes = R.color.success_dark
                }

                TagStatus.DUE_SOON -> {
                    tagView.text = "Por vencer"
                    backgroundDrawableRes = R.drawable.custom_tag_warning
                    darkTextColorRes = R.color.warning_dark
                }

                TagStatus.OVERDUE -> {
                    tagView.text = "Vencidos"
                    backgroundDrawableRes = R.drawable.custom_tag_error
                    darkTextColorRes = R.color.error_dark
                }
            }

            tagView.setBackgroundResource(backgroundDrawableRes)

            val dynamicColor = if (isSelected) {
                ContextCompat.getColor(context, android.R.color.white)
            } else {
                ContextCompat.getColor(context, darkTextColorRes)
            }
            tagView.setTextColor(dynamicColor)

            tagView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            tagView.isSelected = isSelected
        }
    }
}