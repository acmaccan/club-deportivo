package com.example.club_deportivo.ui

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.Activity
import com.google.android.material.card.MaterialCardView

class UpcomingActivityAdapter(private val activitiesWithStatus: List<Pair<Activity, String>>) :
    RecyclerView.Adapter<UpcomingActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_home_upcoming_activity_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (activity, enrollmentStatus) = activitiesWithStatus[position]

        when (enrollmentStatus) {
            "active" -> {
                if (position == 0) {
                    holder.bind(
                        activity,
                        enrollmentStatus,
                        cardColorRes = R.color.primary_light,
                        dotColorRes = R.color.primary_main,
                        textColorRes = R.color.primary_darkest
                    )
                } else {
                    holder.bind(
                        activity,
                        enrollmentStatus,
                        cardColorRes = R.color.secondary_light,
                        dotColorRes = R.color.secondary_main,
                        textColorRes = R.color.secondary_darkest
                    )
                }
            }
            "inactive" -> {
                holder.bind(
                    activity,
                    enrollmentStatus,
                    cardColorRes = R.color.neutral_light,
                    dotColorRes = R.color.neutral_shiny,
                    textColorRes = R.color.neutral_shiny
                )
            }
            "pending" -> {
                holder.bind(
                    activity,
                    enrollmentStatus,
                    cardColorRes = R.color.warning_light,
                    dotColorRes = R.color.warning_main,
                    textColorRes = R.color.warning_dark
                )
            }
        }
    }

    override fun getItemCount(): Int = activitiesWithStatus.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card: MaterialCardView = itemView.findViewById(R.id.card_schedule)
        private val activityName: TextView = itemView.findViewById(R.id.schedule_activity_name)
        private val time: TextView = itemView.findViewById(R.id.schedule_time)
        private val details: TextView = itemView.findViewById(R.id.schedule_details)
        private val colorDot: View = itemView.findViewById(R.id.schedule_color_dot)

        fun bind(activity: Activity, enrollmentStatus: String, cardColorRes: Int, dotColorRes: Int, textColorRes: Int) {
            val context = itemView.context

            val displayName = when (enrollmentStatus) {
                "inactive" -> "${activity.name} - ${context.getString(R.string.home_enrollment_status_blocked)}"
                "pending" -> "${activity.name} - ${context.getString(R.string.home_enrollment_status_pending)}"
                else -> activity.name
            }

            activityName.text = displayName
            time.text = activity.schedule
            details.text = context.getString(
                R.string.home_upcoming_activities_details_format,
                activity.room,
                activity.instructor
            )

            val cardColor = ContextCompat.getColor(context, cardColorRes)
            val dotColor = ContextCompat.getColor(context, dotColorRes)
            val textColor = ContextCompat.getColor(context, textColorRes)

            card.setCardBackgroundColor(cardColor)

            (colorDot.background as? GradientDrawable)?.let {
                it.color = ColorStateList.valueOf(dotColor)
            }

            activityName.setTextColor(textColor)
            time.setTextColor(textColor)
            details.setTextColor(textColor)
        }
    }
}
