package com.example.club_deportivo.models


import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.club_deportivo.R

enum class MembershipStatus(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @ColorRes val iconColor: Int,
    @DrawableRes val iconBackground: Int,
) {
    ENABLED(
        title = R.string.home_membership_enabled,
        icon = R.drawable.icon_check,
        iconColor = R.color.white,
        iconBackground = R.drawable.circle_background_success,
    ),
    DISABLED(
        title = R.string.home_membership_disabled,
        icon = R.drawable.icon_x,
        iconColor = R.color.white,
        iconBackground = R.drawable.circle_background_error,
    );
}