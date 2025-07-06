package com.example.jeeny_case_study_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jeeny_case_study_1.database.DbHelper


class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DbHelper(this)

        val emailEditText = findViewById<EditText>(R.id.email_edittext)
        val passwordEditText = findViewById<EditText>(R.id.password_edittext)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val user = dbHelper.login(email, password)

            if (user != null) {
                val userId = user.first
                val role = user.second

                when (role) {
                    "user" -> {
                        val intent = Intent(this, UserActivity::class.java)
                        intent.putExtra("user_id", userId)
                        startActivity(intent)
                    }
                    "driver" -> {
                        val intent = Intent(this, DriverActivity::class.java)
                        intent.putExtra("user_id", userId)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}