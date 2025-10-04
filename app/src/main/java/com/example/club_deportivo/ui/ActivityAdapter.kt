package com.example.club_deportivo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.Activity

class ActivityAdapter(private val activities: List<Activity>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.activity_name)
        val levelText: TextView = itemView.findViewById(R.id.activity_level_text)
        val duration: TextView = itemView.findViewById(R.id.activity_duration)
        val image: ImageView = itemView.findViewById(R.id.activity_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_activity_card, parent, false)
        return ActivityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        val context = holder.itemView.context

        holder.name.text = activity.name
        holder.image.setImageResource(activity.imageResId)
        holder.duration.text = context.getString(R.string.activity_duration_format, activity.duration)
        holder.levelText.text = context.getString(activity.level.displayNameResId)
    }
}
