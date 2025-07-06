package com.example.jeeny_case_study_1

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jeeny_case_study_1.database.DbHelper

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val dbHelper = DbHelper(this)
        val userId = intent.getIntExtra("user_id", -1)

        val pickupEditText = findViewById<EditText>(R.id.pickup_edittext)
        val dropoffEditText = findViewById<EditText>(R.id.dropoff_edittext)
        val radioGroup = findViewById<RadioGroup>(R.id.radiogroupRideType)
        val requestButton = findViewById<Button>(R.id.request_button)

        requestButton.setOnClickListener {
            val pickup = pickupEditText.text.toString().trim()
            val drop = dropoffEditText.text.toString().trim()
            val selectedRideId = radioGroup.checkedRadioButtonId
            val selectedRideType = findViewById<RadioButton>(selectedRideId).text.toString()

            if (pickup.isEmpty() || drop.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertRide(userId, pickup, drop, selectedRideType)
                if (success) {
                    Toast.makeText(this, "Ride Requested!", Toast.LENGTH_SHORT).show()
                    pickupEditText.text?.clear()
                    dropoffEditText.text?.clear()
                    radioGroup.check(R.id.car_rb)
                }
            }
        }

        val tabRequested = findViewById<LinearLayout>(R.id.tabRequested)
        val tabInprogress = findViewById<LinearLayout>(R.id.tabInProgress)
        val tabCompleted = findViewById<LinearLayout>(R.id.tabCompleted)
        val scrollContainer = findViewById<ScrollView>(R.id.scrollView)
        val parentLayout = findViewById<LinearLayout>(R.id.parent_layout)

        tabRequested.setOnClickListener {

            val rides = dbHelper.getRequestedRides(userId)
            parentLayout.removeAllViews()

            if (rides.isEmpty()) {
                val noData = TextView(this)
                noData.text = "No requested rides"
                noData.textSize = 16f
                noData.setTextColor(Color.BLACK)
                parentLayout.addView(noData)
            } else {
                for (rideInfo in rides) {

                    val parts = rideInfo.split("|")
                    val pickup = parts[0]
                    val drop = parts[1]
                    val type = parts[2]

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

                    card.addView(pickupText)
                    card.addView(dropText)
                    card.addView(typeText)

                    parentLayout.addView(card)
                }
            }
        }

        tabInprogress.setOnClickListener {

            val rides = dbHelper.getInprogressRides(userId)
            parentLayout.removeAllViews() // clear old views

            if (rides.isEmpty()) {
                val noData = TextView(this)
                noData.text = "No in-progress rides"
                noData.textSize = 16f
                noData.setTextColor(Color.BLACK)
                parentLayout.addView(noData)
            } else {
                for (rideInfo in rides) {
                    // Split info
                    val parts = rideInfo.split("|")
                    val pickup = parts[0]
                    val drop = parts[1]
                    val type = parts[2]

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

                    card.addView(pickupText)
                    card.addView(dropText)
                    card.addView(typeText)

                    parentLayout.addView(card)
                }
            }
        }

        tabCompleted.setOnClickListener {

            val rides = dbHelper.getCompletedRides(userId)
            parentLayout.removeAllViews() // clear old views

            if (rides.isEmpty()) {
                val noData = TextView(this)
                noData.text = "No completed rides"
                noData.textSize = 16f
                noData.setTextColor(Color.BLACK)
                parentLayout.addView(noData)
            } else {
                for (rideInfo in rides) {
                    // Split info
                    val parts = rideInfo.split("|")
                    val pickup = parts[0]
                    val drop = parts[1]
                    val type = parts[2]

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

                    card.addView(pickupText)
                    card.addView(dropText)
                    card.addView(typeText)

                    parentLayout.addView(card)
                }
            }
        }

    }

}