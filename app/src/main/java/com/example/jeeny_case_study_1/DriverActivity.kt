package com.example.jeeny_case_study_1

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jeeny_case_study_1.database.DbHelper


class DriverActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelper
    private lateinit var rideContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        dbHelper = DbHelper(this)
        rideContainer = findViewById(R.id.driver_ride_list)

        val tabRequested = findViewById<LinearLayout>(R.id.tabRequested)
        val tabAccepted = findViewById<LinearLayout>(R.id.tabInProgress)
        val tabCompleted = findViewById<LinearLayout>(R.id.tabCompleted)

        tabRequested.setOnClickListener { loadRides("requested") }
        tabAccepted.setOnClickListener { loadRides("in-progress") }
        tabCompleted.setOnClickListener { loadRides("completed") }

        loadRides("requested") // default tab
    }

    private fun loadRides(status: String) {
        val rides = dbHelper.getRidesByStatus(status)
        rideContainer.removeAllViews()

        if (rides.isEmpty()) {
            val tv = TextView(this)
            tv.text = "No $status rides"
            tv.textSize = 16f
            tv.setTextColor(Color.BLACK)
            rideContainer.addView(tv)
        } else {
            for ((pickup, drop, type) in rides) {
                val card = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(32, 32, 32, 32)
                    background = resources.getDrawable(android.R.drawable.dialog_holo_light_frame)
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 32)
                    layoutParams = params
                    elevation = 4f
                }

                val pickupText = TextView(this).apply {
                    text = "Pickup: $pickup"
                    textSize = 16f
                    setTextColor(Color.BLACK)
                }

                val dropText = TextView(this).apply {
                    text = "Dropoff: $drop"
                    textSize = 16f
                    setTextColor(Color.BLACK)
                }

                val typeText = TextView(this).apply {
                    text = "Type: $type"
                    textSize = 16f
                    setTextColor(Color.BLACK)
                }

                val actionButton = Button(this).apply {
                    text = when (status) {
                        "requested" -> "Accept Ride"
                        "in-progress" -> "Complete Ride"
                        else -> ""
                    }
                    visibility = if (status == "completed") android.view.View.GONE else android.view.View.VISIBLE
                    setBackgroundColor(Color.parseColor("#F61BF6"))
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        val newStatus = if (status == "requested") "in-progress" else "completed"
                        dbHelper.updateRideStatus(pickup, drop, status, newStatus)
                        loadRides(status) // refresh current tab
                        Toast.makeText(this@DriverActivity, "Ride updated to $newStatus", Toast.LENGTH_SHORT).show()
                    }
                }

                card.addView(pickupText)
                card.addView(dropText)
                card.addView(typeText)
                if (actionButton.visibility == android.view.View.VISIBLE)
                    card.addView(actionButton)

                rideContainer.addView(card)
            }
        }
    }
}