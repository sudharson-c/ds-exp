package com.example.hotel

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bookingStatus: TextView
    private lateinit var roomButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookingStatus = findViewById(R.id.bookingStatus)
        roomButton = findViewById(R.id.roomButton)
        progressBar = findViewById(R.id.progressBar)

        registerForContextMenu(bookingStatus)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE)

        // Check if username is already stored
        val username = sharedPref.getString("username", null)
        if (username == null) {
            // Show dialog to get username
            showUsernameDialog()
        } else {
            // Welcome back the user
            Toast.makeText(this, "Welcome back, $username!", Toast.LENGTH_SHORT).show()
        }

        // Popup Menu
        roomButton.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.view_details -> viewBookingDetails()
                    R.id.book_now -> confirmBooking()
                    R.id.change_name -> showUsernameDialog()
                }
                true
            }
            popup.show()
        }

        // Progress Bar Simulation
        progressBar.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            bookingStatus.text = getString(R.string.room_available)
        }, 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_booking -> startActivity(Intent(this, BookingActivity::class.java))
            R.id.cancel_booking -> startActivity(Intent(this, BookingActivity::class.java))
        }
        return true
    }

    private fun showAddress() {
        lifecycleScope.launch {
            val addressList = withContext(Dispatchers.IO) {
                val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                geocoder.getFromLocation(13.0827, 80.2707, 1)
            }
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0].getAddressLine(0)
                bookingStatus.text = getString(R.string.hotel_location, address)
            }
        }
    }

    private fun showUsernameDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_username, null)
        val editTextUsername = dialogView.findViewById<EditText>(R.id.editTextUsername)

        AlertDialog.Builder(this)
            .setTitle("Enter Your Username")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val username = editTextUsername.text.toString()
                if (username.isNotEmpty()) {
                    // Save username in SharedPreferences
                    val editor = sharedPref.edit()
                    editor.putString("username", username)
                    editor.apply()

                    Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setCancelable(false) // Prevent closing the dialog without entering a username
            .show()
    }

    private fun confirmBooking() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_title))
            .setMessage(getString(R.string.confirm_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // Save booking details
                val editor = sharedPref.edit()
                editor.putString("room_number", "101") // Example room number
                editor.putString("booking_date", System.currentTimeMillis().toString()) // Current timestamp
                editor.apply()

                Toast.makeText(this, getString(R.string.booking_success), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()
        alertDialog.show()
    }

    private fun viewBookingDetails() {
        val roomNumber = sharedPref.getString("room_number", "No booking found")
        val bookingDate = sharedPref.getString("booking_date", "")

        val intent = Intent(this, BookingDetailsActivity::class.java).apply {
            putExtra("room_number", roomNumber)
            putExtra("booking_date", bookingDate)
        }
        startActivity(intent)
    }
}
