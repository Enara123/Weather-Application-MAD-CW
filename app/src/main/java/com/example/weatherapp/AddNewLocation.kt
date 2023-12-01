package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddNewLocation : AppCompatActivity() {

    private lateinit var txtNewCity : EditText
    private lateinit var btnAdd : Button
    private lateinit var btnBack : Button

    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var cityArrayList : ArrayList<City>

    private lateinit var database : DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_location)

        txtNewCity = findViewById(R.id.txt_NewCity)
        btnAdd = findViewById(R.id.btn_add)
        btnBack = findViewById(R.id.btn_back)

        cityRecyclerView = findViewById(R.id.recycler_view)
        cityRecyclerView.layoutManager = LinearLayoutManager(this)
        cityRecyclerView.setHasFixedSize(true)


        cityArrayList = arrayListOf<City>()
        getCityData()

        btnAdd.setOnClickListener{
            addNewCityToDB()
        }

        btnBack.setOnClickListener(){
            val intent = Intent(this,SelectedLocationWeather.SelectedLocationWeather::class.java)
            startActivity(intent)
        }

    }

    private fun addNewCityToDB(){

        val newCityName = txtNewCity.text.toString().trim()
        database = FirebaseDatabase.getInstance().reference

        Log.d("AddNewLocation", "New City Name: $newCityName")

        if (newCityName.isNotEmpty()){
            val cityKey = database.child("cities").push().key
            val city = City(newCityName)
            if (cityKey != null){
                database.child("cities").child(cityKey).setValue(city)
                txtNewCity.text.clear()
            }
        }

    }

    private fun getCityData() {
        database = FirebaseDatabase.getInstance().getReference("cities")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cityArrayList.clear()
                for (snapshot in dataSnapshot.children) {
                    val city = snapshot.getValue(City::class.java )
                    cityArrayList.add(city!!)
                }

                cityRecyclerView.adapter = CityAdapter(cityArrayList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Database", "Error")
            }
        })
    }
}
