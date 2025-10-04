package com.example.club_deportivo.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.club_deportivo.R

enum class ActionCardStyle(
    @DrawableRes val background: Int,
    @ColorRes val iconTint: Int
) {
    PRIMARY(
        background = R.drawable.circle_background_primary_light,
        iconTint = R.color.primary_main
    ),
    SECONDARY(
        background = R.drawable.circle_background_secondary_light,
        iconTint = R.color.secondary_main
    ),
    SUCCESS(
        background = R.drawable.circle_background_success_light,
        iconTint = R.color.success_main
    ),
    ERROR(
        background = R.drawable.circle_background_error_light,
        iconTint = R.color.error_main
    )
}
