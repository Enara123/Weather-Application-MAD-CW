package com.example.weatherapp

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class CurrentLocationWeather : AppCompatActivity() {

    private lateinit var locationRequest : LocationRequest

    private val locationClient : FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    var currentLocation : Location? = null

    private lateinit var lblLocation : TextView
    private lateinit var lblLatLong : TextView

    private lateinit var lblDescription :TextView
    private lateinit var lblHumidity :TextView
    private lateinit var lblPressure :TextView
    private lateinit var lblTemperature :TextView
    private lateinit var lblWindSpeed :TextView
    private lateinit var imgWeather : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location_weather)


        lblLocation = findViewById(R.id.lbl_location)
        lblLatLong = findViewById(R.id.lbl_latlon)

        lblDescription = findViewById(R.id.lbl_description)
        lblHumidity = findViewById(R.id.lbl_humidity)
        lblPressure = findViewById(R.id.lbl_pressure)
        lblTemperature = findViewById(R.id.lbl_temp)
        lblWindSpeed = findViewById(R.id.lbl_windSpeed)
        imgWeather = findViewById(R.id.imageView)

        checkPermission()
    }

    //Check permission for location access
    private fun checkPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            accessLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
        }

    }

    //Access location if permission granted
    @SuppressLint("MissingPermission")
    fun accessLocation(){
        //how much accuracy and frequency
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100).build()

        val locationCallBack = object : LocationCallback() {
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                p0.locations.lastOrNull()?. let { location ->
                    val currentLatitude = location.latitude
                    val currentLongitude = location.longitude

                    getWeatherInfo(currentLatitude, currentLongitude)

                    lblLatLong.text = "Latitude : " +currentLatitude + " Longitude : " +currentLongitude
                }
            }
        }

        locationClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())

    }

    //Get information from API
    @SuppressLint("SetTextI18n")
    fun getWeatherInfo(latitude: Double, longitude: Double){

        Log.e("API", "Api Called")
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=b07113e56b9d36ac3723fb77cfe44797"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null, {
                    response ->
                lblDescription.text =
                    "Description : "+response.getJSONArray("weather").getJSONObject(0).getString("description")
                lblTemperature.text = "Temperature : "+response.getJSONObject("main").getString("temp")
                lblPressure.text = "Pressure : "+response.getJSONObject("main").getString("pressure")
                lblHumidity.text = "Humidity : "+response.getJSONObject("main").getString("humidity")
                lblWindSpeed.text = "Wind Speed : "+response.getJSONObject("wind").getString("speed")

                val imageURL = "https://openweathermap.org/img/w/" + response.getJSONArray("weather").getJSONObject(0).getString("icon")+".png"

                Picasso.get().load(imageURL).into(imgWeather)
            },
            { error ->
                Log.e("API", "Response Errors")
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(request)

    }

}