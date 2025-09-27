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

        fun setupFilterTag(tagView: TextView, status: TagStatus = TagStatus.PAID, context: Context, isSelected: Boolean = false) {
            when (status) {
                TagStatus.PAID -> {
                    tagView.text = "Al día"
                    tagView.setBackgroundResource(R.drawable.custom_tag_success)
                }
                TagStatus.DUE_SOON -> {
                    tagView.text = "Por vencer"
                    tagView.setBackgroundResource(R.drawable.custom_tag_warning)
                }
                TagStatus.OVERDUE -> {
                    tagView.text = "Vencidos"
                    tagView.setBackgroundResource(R.drawable.custom_tag_error)
                }
            }

            tagView.isSelected = isSelected

            if (isSelected) {
                tagView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            } else {
                val darkColor = when (status) {
                    TagStatus.PAID -> R.color.success_dark
                    TagStatus.DUE_SOON -> R.color.warning_dark
                    TagStatus.OVERDUE -> R.color.error_dark
                }
                tagView.setTextColor(ContextCompat.getColor(context, darkColor))
            }
        }
    }
}