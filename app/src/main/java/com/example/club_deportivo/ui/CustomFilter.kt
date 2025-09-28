package com.example.club_deportivo.ui

import android.content.Context
import android.widget.TextView
import com.example.club_deportivo.models.PaymentStatus

class CustomFilter(
    private val context: Context,
    private val paidFilter: TextView,
    private val dueSoonFilter: TextView,
    private val overdueFilter: TextView,
    private val onFilterChanged: (Set<PaymentStatus>) -> Unit
) {
    private var activeFilters = mutableSetOf(PaymentStatus.PAID, PaymentStatus.DUE_SOON, PaymentStatus.OVERDUE)

    init {
        setupFilters()
    }

    private fun setupFilters() {
        paidFilter.setOnClickListener { toggleFilterState(PaymentStatus.PAID) }
        dueSoonFilter.setOnClickListener { toggleFilterState(PaymentStatus.DUE_SOON) }
        overdueFilter.setOnClickListener { toggleFilterState(PaymentStatus.OVERDUE) }

        updateFilterViews()
    }

    private fun toggleFilterState(filterType: PaymentStatus) {
        if (activeFilters.contains(filterType)) {
            activeFilters.remove(filterType)
        } else {
            activeFilters.add(filterType)
        }

        updateFilterViews()

        onFilterChanged(activeFilters)
    }

    private fun updateFilterViews() {
        StatusTagHelper.setupFilterTag(
            paidFilter, PaymentStatus.PAID, context,
            isSelected = activeFilters.contains(PaymentStatus.PAID)
        )

        StatusTagHelper.setupFilterTag(
            dueSoonFilter, PaymentStatus.DUE_SOON, context,
            isSelected = activeFilters.contains(PaymentStatus.DUE_SOON)
        )

        StatusTagHelper.setupFilterTag(
            overdueFilter, PaymentStatus.OVERDUE, context,
            isSelected = activeFilters.contains(PaymentStatus.OVERDUE)
        )
    }
}
