package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddNewLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_location)

        val database = FirebaseDatabase.getInstance()
        val citiesRef = database.getReference("cities")

        citiesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val citiesList = ArrayList<City>()

                for (citySnapshot in dataSnapshot.children) {
                    val cityName = citySnapshot.getValue(String::class.java)
                    val city = City(cityName ?: "")
                    citiesList.add(city)
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                
            }
        })
    }
}