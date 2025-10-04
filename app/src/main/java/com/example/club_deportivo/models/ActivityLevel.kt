package com.example.club_deportivo.models

import androidx.annotation.StringRes
import com.example.club_deportivo.R

enum class ActivityLevel(@StringRes val displayNameResId: Int) {
    BEGINNER(R.string.activity_level_beginner),
    INTERMEDIATE(R.string.activity_level_intermediate),
    ADVANCED(R.string.activity_level_advanced)
}