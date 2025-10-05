package com.example.club_deportivo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.PaymentActivity

class PaymentActivityAdapter(
    private val activities: List<PaymentActivity>,
    private val onSelectionChanged: (PaymentActivity?) -> Unit
) : RecyclerView.Adapter<PaymentActivityAdapter.PaymentActivityViewHolder>() {

    private var selectedPosition = -1

    class PaymentActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.activityRadioButton)
        val activityName: TextView = itemView.findViewById(R.id.activityName)
        val instructorName: TextView = itemView.findViewById(R.id.instructorName)
        val schedule: TextView = itemView.findViewById(R.id.activitySchedule)
        val price: TextView = itemView.findViewById(R.id.activityPrice)
        val priceLabel: TextView = itemView.findViewById(R.id.activityPriceLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_payment_activity_item, parent, false)
        return PaymentActivityViewHolder(view)
    }

    override fun getItemCount(): Int = activities.size

    override fun onBindViewHolder(holder: PaymentActivityViewHolder, position: Int) {
        val activity = activities[position]
        val context = holder.itemView.context

        holder.activityName.text = activity.name
        holder.instructorName.text = activity.instructor
        holder.schedule.text = activity.schedule
        holder.price.text = context.getString(R.string.payments_amount_format, activity.monthlyPrice)
        holder.priceLabel.text = context.getString(R.string.payments_per_month)

        holder.radioButton.isChecked = position == selectedPosition

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            
            if (previousPosition != -1) {
                notifyItemChanged(previousPosition)
            }
            notifyItemChanged(selectedPosition)
            
            onSelectionChanged(activity)
        }

        holder.radioButton.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            
            if (previousPosition != -1) {
                notifyItemChanged(previousPosition)
            }
            notifyItemChanged(selectedPosition)
            
            onSelectionChanged(activity)
        }
    }

    fun getSelectedActivity(): PaymentActivity? {
        return if (selectedPosition != -1) activities[selectedPosition] else null
    }
}