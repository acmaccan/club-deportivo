package com.example.club_deportivo.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.club_deportivo.R
import com.example.club_deportivo.models.Client
import com.example.club_deportivo.ui.CustomProfileMembershipCard

class ProfileActivity : BaseAuthActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val clientUser = user as? Client
        if (clientUser == null) {
            finish()
            return
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val membershipCardView: CustomProfileMembershipCard = findViewById(R.id.profile_membership_card)
        membershipCardView.setup(clientUser)

        setupPersonalInfo(clientUser)
    }

    private fun setupPersonalInfo(client: Client) {
        findViewById<LinearLayout>(R.id.info_name).apply {
            findViewById<ImageView>(R.id.info_icon).setImageResource(R.drawable.icon_person)
            findViewById<TextView>(R.id.info_label).text = getString(R.string.name_and_surname)
            findViewById<TextView>(R.id.info_value).text = client.name
        }

        findViewById<LinearLayout>(R.id.info_document).apply {
            findViewById<ImageView>(R.id.info_icon).setImageResource(R.drawable.icon_person)
            findViewById<TextView>(R.id.info_label).text = getString(R.string.document)
            findViewById<TextView>(R.id.info_value).text = client.document
        }

        findViewById<LinearLayout>(R.id.info_email).apply {
            findViewById<ImageView>(R.id.info_icon).setImageResource(R.drawable.icon_person)
            findViewById<TextView>(R.id.info_label).text = getString(R.string.email)
            findViewById<TextView>(R.id.info_value).text = client.email
        }
    }
}
