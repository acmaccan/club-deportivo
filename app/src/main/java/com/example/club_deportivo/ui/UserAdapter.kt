package com.example.club_deportivo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.Client
import com.google.android.material.card.MaterialCardView

class UserAdapter(
    private var users: List<Client>,
    private val onPayClick: (String) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(val cardView: MaterialCardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_card_payment_status, parent, false) as MaterialCardView
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        CustomPaymentStatusCard.setup(holder.cardView, user, onPayClick)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateUsers(newUsers: List<Client>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
