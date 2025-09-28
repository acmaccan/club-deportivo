package com.example.club_deportivo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.ui.CustomHeader

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userName = "Kevin Del Bello"
        CustomHeader.setupHomeHeader(this, userName)
    }
}
