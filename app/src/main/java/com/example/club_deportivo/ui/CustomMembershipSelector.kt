package com.example.club_deportivo.ui

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.MembershipType
import com.google.android.material.card.MaterialCardView

object CustomMembershipSelector {
    
    fun setup(
        memberCard: MaterialCardView,
        noMemberCard: MaterialCardView,
        onSelectionChanged: (MembershipType?) -> Unit
    ) {
        var selectedMembershipType: MembershipType? = null
        
        memberCard.setOnClickListener {
            selectedMembershipType = MembershipType.MEMBER
            updateCardStates(memberCard, noMemberCard, selectedMembershipType)
            onSelectionChanged(selectedMembershipType)
        }
        
        noMemberCard.setOnClickListener {
            selectedMembershipType = MembershipType.NO_MEMBER
            updateCardStates(memberCard, noMemberCard, selectedMembershipType)
            onSelectionChanged(selectedMembershipType)
        }
        
        // Initialize with no selection
        updateCardStates(memberCard, noMemberCard, null)
    }
    
    private fun updateCardStates(
        memberCard: MaterialCardView,
        noMemberCard: MaterialCardView,
        selectedType: MembershipType?
    ) {
        updateSingleCard(memberCard, selectedType == MembershipType.MEMBER, R.id.memberIcon, R.id.memberIconBackground)
        updateSingleCard(noMemberCard, selectedType == MembershipType.NO_MEMBER, R.id.noMemberIcon, R.id.noMemberIconBackground)
    }
    
    private fun updateSingleCard(
        card: MaterialCardView,
        isSelected: Boolean,
        iconId: Int,
        backgroundId: Int
    ) {
        val context = card.context
        val icon = card.findViewById<ImageView>(iconId)
        val iconBackground = card.findViewById<View>(backgroundId)
        
        if (isSelected) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_light))
            card.strokeColor = ContextCompat.getColor(context, R.color.primary_main)
            icon.visibility = View.VISIBLE
            icon.imageTintList = ContextCompat.getColorStateList(context, R.color.white)
            iconBackground.visibility = View.VISIBLE
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            card.strokeColor = ContextCompat.getColor(context, R.color.primary_light)
            icon.visibility = View.VISIBLE
            icon.imageTintList = ContextCompat.getColorStateList(context, R.color.primary_main)
            iconBackground.visibility = View.INVISIBLE
        }
    }
}