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
        fun setupTag(tagView: TextView, status: TagStatus, context: Context, isEnabled: Boolean = true) {
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

        fun setupFilterTag(tagView: TextView, status: TagStatus = TagStatus.PAID, context: Context, isSelected: Boolean = true) {
            tagView.text = when (status) {
                TagStatus.PAID -> "Al día"
                TagStatus.DUE_SOON -> "Por vencer"
                TagStatus.OVERDUE -> "Vencidos"
            }

            val backgroundDrawable: Int
            val darkTextColor: Int

            when (status) {
                TagStatus.PAID -> {
                    backgroundDrawable = R.drawable.custom_tag_success
                    darkTextColor = R.color.success_dark
                }
                TagStatus.DUE_SOON -> {
                    backgroundDrawable = R.drawable.custom_tag_warning
                    darkTextColor = R.color.warning_dark
                }
                TagStatus.OVERDUE -> {
                    backgroundDrawable = R.drawable.custom_tag_error
                    darkTextColor = R.color.error_dark
                }
            }

            tagView.setBackgroundResource(backgroundDrawable)

            if (isSelected) {
                tagView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            } else {
                tagView.setTextColor(ContextCompat.getColor(context, darkTextColor))
            }

            tagView.isSelected = isSelected
        }
    }
}