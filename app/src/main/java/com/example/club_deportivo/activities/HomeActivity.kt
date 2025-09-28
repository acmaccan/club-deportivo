package com.example.club_deportivo.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.models.MembershipStatus
import com.example.club_deportivo.ui.CustomHeader
import com.google.android.material.card.MaterialCardView
import com.example.club_deportivo.models.PaymentStatus
import com.example.club_deportivo.models.UserRepository
import com.example.club_deportivo.ui.CustomCardMembership

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val loggedUserId = intent.getIntExtra("LOGGED_USER_ID", -1)

        if (loggedUserId == -1) {
            Toast.makeText(this, "Error: No se pudo identificar al usuario.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val user = UserRepository.getClients().find { it.id == loggedUserId }

        if (user == null) {
            Toast.makeText(this, "Error: Usuario no encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        CustomHeader.setupHomeHeader(this, user.name)

        val membershipStatus = if (user.status == PaymentStatus.OVERDUE) {
            MembershipStatus.DISABLED
        } else {
            MembershipStatus.ENABLED
        }

        val membershipCardView = findViewById<MaterialCardView>(R.id.membershipStatusCard)
        CustomCardMembership.setup(membershipCardView, membershipStatus, user.membershipType)
    }
}
