package com.example.getlocationapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, OnSuccessListener<Location> {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var longitude : TextView
    private lateinit var latitude : TextView
    private lateinit var spLongitude : TextView
    private lateinit var spLatitude : TextView
    private lateinit var btnLocation: Button
    private lateinit var btnSave : Button
    private lateinit var btnShow : Button
    private var REQUEST_CODE: Int = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        latitude = findViewById(R.id.edit_latitude)
        longitude = findViewById(R.id.edit_longitude)
        spLatitude = findViewById(R.id.edit_sp_latitude)
        spLongitude = findViewById(R.id.edit_sp_longitude)
        btnLocation = findViewById(R.id.btn_get_location)
        btnSave = findViewById(R.id.btn_save)
        btnShow = findViewById(R.id.btn_show)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        btnLocation.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnShow.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v == this.btnLocation) {
            getLastLocation()

        }else if(v == this.btnSave){
            saveSharedPreference()
        }else if(v == this.btnShow){
            getSharedPreference()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }else{
                Toast.makeText(this, "Need Permission", Toast.LENGTH_LONG)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun getLastLocation(){
        var accessLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(accessLocation == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this)
        }else{
            askPermition()
        }
    }


    override fun onSuccess(location: Location?) {
        if (location != null){
            var geocoder = Geocoder(this, Locale.getDefault())
            var address : List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            latitude.text = "Latitude: ${address.get(0).latitude}"
            longitude.text = "Longitude: ${address.get(0).longitude}"

        }
    }

    private fun askPermition() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }


    private fun saveSharedPreference() {
        val sp = this.getSharedPreferences("mainPrefs", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("latitude", latitude.text.toString())
        editor.putString("longitude", longitude.text.toString())
        editor.commit()
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }

    private fun getSharedPreference(){
        val sp = this.getSharedPreferences("mainPrefs", MODE_PRIVATE)
        spLatitude.text = "${sp.getString("latitude", "")}"
        spLongitude.text = "${sp.getString("longitude", "")}"

    }


}


