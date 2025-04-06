package com.example.hotel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        val roomNumber = intent.getStringExtra("room_number")
        val bookingDate = intent.getStringExtra("booking_date")

        val roomNumberTextView: TextView = findViewById(R.id.roomNumberTextView)
        val bookingDateTextView: TextView = findViewById(R.id.bookingDateTextView)

        roomNumberTextView.text = "Room Number: $roomNumber"
        bookingDateTextView.text = "Booking Date: $bookingDate"
    }
} 