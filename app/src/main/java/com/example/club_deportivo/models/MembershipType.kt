package com.example.club_deportivo.models

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.club_deportivo.R

enum class MembershipType(
    @StringRes val displayName: Int,
    @ColorRes val cardBackgroundColor: Int,
    @StringRes val textTitle: Int,
    @ColorRes val titleColor: Int,
    @StringRes val textDescription: Int,
    @ColorRes val descriptionColor: Int,
    @StringRes val benefits: List<Int>
) {
    MEMBER(
        displayName = R.string.membership_type_member,
        cardBackgroundColor = R.color.primary_light,
        textTitle = R.string.membership_title_member,
        titleColor = R.color.primary_darkest,
        textDescription = R.string.membership_description_member,
        descriptionColor = R.color.primary_main,
        benefits = listOf(
            R.string.profile_benefit_member_1,
            R.string.profile_benefit_member_2,
            R.string.profile_benefit_member_3,
            R.string.profile_benefit_member_4
        )
    ),
    NO_MEMBER(
        displayName = R.string.membership_type_no_member,
        cardBackgroundColor = R.color.secondary_light,
        textTitle = R.string.membership_title_no_member,
        titleColor = R.color.secondary_darkest,
        textDescription = R.string.membership_description_no_member,
        descriptionColor = R.color.secondary_main,
        benefits = listOf(
            R.string.profile_benefit_no_member_1,
            R.string.profile_benefit_no_member_2
        )
    );
}