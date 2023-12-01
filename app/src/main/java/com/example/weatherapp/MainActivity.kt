package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var btnCurrentWeather : Button
    private lateinit var btnSelectCity : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        btnCurrentWeather = findViewById(R.id.btn_currentWeather)
        btnSelectCity = findViewById(R.id.btn_selectCity)

        btnCurrentWeather.setOnClickListener(){
            val intent = Intent(this,CurrentLocationWeather::class.java)
            startActivity(intent)
        }

        btnSelectCity.setOnClickListener(){
            val intent = Intent(this,SelectedLocationWeather.SelectedLocationWeather::class.java)
            startActivity(intent)
        }

    }
}