package com.example.jeeny_case_study_1.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, "jeeny.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT,
                password TEXT,
                role TEXT
            )
        """
        db.execSQL(createUserTable)

        // Dummy users added
        db.execSQL("INSERT INTO users (name, email, password, role) VALUES ('User One', 'user@user.com', 'user123', 'user')")
        db.execSQL("INSERT INTO users (name, email, password, role) VALUES ('Driver One', 'driver@driver.com', 'driver123', 'driver')")

        val createRidesTable = """
            CREATE TABLE rides (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                passenger_id TEXT,
                pickup_location TEXT,
                drop_location TEXT,
                ride_type TEXT,
                status TEXT
            )
        """
        db.execSQL(createRidesTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun login(email: String, password: String): Pair<Int, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, role FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            val role = cursor.getString(1)
            cursor.close()
            Pair(id, role)
        } else {
            cursor.close()
            null
        }
    }

    fun insertRide(passengerId: Int, pickup: String, drop: String, rideType: String): Boolean {
        val db = writableDatabase
        val insertSQL = """
        INSERT INTO rides (passenger_id, pickup_location, drop_location, ride_type, status)
        VALUES (?, ?, ?, ?, ?)
    """
        db.execSQL(insertSQL, arrayOf(passengerId.toString(), pickup, drop, rideType, "requested"))
        return true
    }

    fun getRequestedRides(userId: Int): List<String> {
        val db = readableDatabase
        val rides = mutableListOf<String>()
        val cursor = db.rawQuery(
            "SELECT pickup_location, drop_location, ride_type FROM rides WHERE passenger_id=? AND status='requested'",
            arrayOf(userId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val pickup = cursor.getString(0)
                val drop = cursor.getString(1)
                val type = cursor.getString(2)
                rides.add("$pickup|$drop|$type")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return rides
    }

    fun getInprogressRides(userId: Int): List<String> {
        val db = readableDatabase
        val rides = mutableListOf<String>()
        val cursor = db.rawQuery(
            "SELECT pickup_location, drop_location, ride_type FROM rides WHERE passenger_id=? AND status='in-progress'",
            arrayOf(userId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val pickup = cursor.getString(0)
                val drop = cursor.getString(1)
                val type = cursor.getString(2)
                rides.add("$pickup|$drop|$type")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return rides
    }


    fun getCompletedRides(userId: Int): List<String> {
        val db = readableDatabase
        val rides = mutableListOf<String>()
        val cursor = db.rawQuery(
            "SELECT pickup_location, drop_location, ride_type FROM rides WHERE passenger_id=? AND status='completed'",
            arrayOf(userId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val pickup = cursor.getString(0)
                val drop = cursor.getString(1)
                val type = cursor.getString(2)
                rides.add("$pickup|$drop|$type")
            } while (cursor.moveToNext())
        }

        cursor.close()
        return rides
    }

    fun updateRideStatus(pickup: String, drop: String, oldStatus: String, newStatus: String): Boolean {
        val db = writableDatabase
        val query = """
        UPDATE rides 
        SET status = ? 
        WHERE pickup_location = ? AND drop_location = ? AND status = ?
    """
        db.execSQL(query, arrayOf(newStatus, pickup, drop, oldStatus))
        return true
    }

    fun getRidesByStatus(status: String): List<Triple<String, String, String>> {
        val db = readableDatabase
        val rides = mutableListOf<Triple<String, String, String>>()

        val cursor = db.rawQuery(
            "SELECT pickup_location, drop_location, ride_type FROM rides WHERE status=?",
            arrayOf(status)
        )

        if (cursor.moveToFirst()) {
            do {
                val pickup = cursor.getString(0)
                val drop = cursor.getString(1)
                val type = cursor.getString(2)
                rides.add(Triple(pickup, drop, type))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return rides
    }







}
