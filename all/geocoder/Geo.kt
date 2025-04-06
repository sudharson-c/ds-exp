import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var edtAddress: EditText
    lateinit var btnGetCoordinates: Button
    lateinit var txtResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtAddress = findViewById(R.id.edtAddress)
        btnGetCoordinates = findViewById(R.id.btnGetCoordinates)
        txtResult = findViewById(R.id.txtResult)

        btnGetCoordinates.setOnClickListener {
            val address = edtAddress.text.toString()
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(address, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val location = addresses[0]
                txtResult.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
            } else {
                txtResult.text = "Location not found"
            }
        }
    }
}