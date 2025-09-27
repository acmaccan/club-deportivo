package com.example.club_deportivo.ui

import android.content.Context
import android.widget.TextView

class FilterManager(
    private val context: Context,
    private val paidFilter: TextView,
    private val dueSoonFilter: TextView,
    private val overdueFilter: TextView,
    private val onFilterChanged: (Set<TagStatus>) -> Unit
) {
    private var activeFilters = mutableSetOf(TagStatus.PAID, TagStatus.DUE_SOON, TagStatus.OVERDUE)

    init {
        setupFilters()
    }

    private fun setupFilters() {
        paidFilter.setOnClickListener { toggleFilterState(TagStatus.PAID) }
        dueSoonFilter.setOnClickListener { toggleFilterState(TagStatus.DUE_SOON) }
        overdueFilter.setOnClickListener { toggleFilterState(TagStatus.OVERDUE) }

        updateFilterViews()
    }

    private fun toggleFilterState(filterType: TagStatus) {
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
            paidFilter, TagStatus.PAID, context,
            isSelected = activeFilters.contains(TagStatus.PAID)
        )

        StatusTagHelper.setupFilterTag(
            dueSoonFilter, TagStatus.DUE_SOON, context,
            isSelected = activeFilters.contains(TagStatus.DUE_SOON)
        )

        StatusTagHelper.setupFilterTag(
            overdueFilter, TagStatus.OVERDUE, context,
            isSelected = activeFilters.contains(TagStatus.OVERDUE)
        )
    }
}
