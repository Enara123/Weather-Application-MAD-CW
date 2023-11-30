package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SelectedLocationWeather : AppCompatActivity() {

class SelectedLocationWeather : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //Weather
    private lateinit var spnCity: Spinner
    private lateinit var lblDescription: TextView
    private lateinit var lblHumidity: TextView
    private lateinit var lblPressure: TextView
    private lateinit var lblTemperature: TextView
    private lateinit var lblWindSpeed: TextView
    private lateinit var imgWeather: ImageView
    private val city =
        arrayOf("Colombo", "London", "Tokyo", "Hong Kong", "Manila", "Seoul", "Munich", "Paris")

    //RecyclerView for day forecast
    private lateinit var forecastRecycles: RecyclerView
    var forecastData = JSONArray()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_location_weather)

        //Weather
        spnCity = findViewById(R.id.spn_city)
        lblDescription = findViewById(R.id.lbl_description)
        lblHumidity = findViewById(R.id.lbl_humidity)
        lblPressure = findViewById(R.id.lbl_pressure)
        lblTemperature = findViewById(R.id.lbl_temp)
        lblWindSpeed = findViewById(R.id.lbl_windSpeed)
        imgWeather = findViewById(R.id.imageView)

        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, city)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spnCity.adapter = cityAdapter
        spnCity.onItemSelectedListener = this

        //Forecast
        forecastRecycles = findViewById(R.id.forecastRecycle)
        forecastRecycles.adapter = ForecastAdapter()
        forecastRecycles.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )

    }

    //Spn city listener
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        getCityWeatherInfo(city[position])
        fetchForecastData(city[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    //Get Weather information about selected city from API
    @SuppressLint("SetTextI18n")
    fun getCityWeatherInfo(city: String) {
        Log.e("API", "Api Called")
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=b07113e56b9d36ac3723fb77cfe44797"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
                lblDescription.text =
                    "Description : " + response.getJSONArray("weather").getJSONObject(0)
                        .getString("description")
                lblTemperature.text =
                    "Temperature : " + response.getJSONObject("main").getString("temp")
                lblPressure.text =
                    "Pressure : " + response.getJSONObject("main").getString("pressure")
                lblHumidity.text =
                    "Humidity : " + response.getJSONObject("main").getString("humidity")
                lblWindSpeed.text =
                    "Wind Speed : " + response.getJSONObject("wind").getString("speed")

                val imageURL =
                    "https://openweathermap.org/img/w/" + response.getJSONArray("weather")
                        .getJSONObject(0).getString("icon") + ".png"

                Picasso.get().load(imageURL).into(imgWeather)
            },
            { error ->
                Log.e("API", "Response Errors")
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(request)
    }


    //Fetching forecast data from API
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchForecastData(city: String) {
        Log.e("API", "Api Called")
        val url =
            "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=b07113e56b9d36ac3723fb77cfe44797"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null, Response.Listener { res ->
                forecastData = res.getJSONArray("list")
                forecastRecycles.adapter?.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Log.e("API", "Response Errors")
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    //Setting up RecyclerView to fetch data
    private inner class ForecastAdapter : RecyclerView.Adapter<ForecastDataHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDataHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.day_forcast, parent, false)

            return ForecastDataHolder(view)
        }

        override fun getItemCount(): Int {
            return forecastData.length()
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ForecastDataHolder, position: Int) {
            holder.forecastDay.text = forecastData.getJSONObject(position).getString("dt_txt")

            val imageUrl =
                "https://openweathermap.org/img/w/" + forecastData.getJSONObject(position)
                    .getJSONArray("weather").getJSONObject(0).getString("icon") + ".png"
            Picasso.get().load(imageUrl).into(holder.dayImage)

            holder.forecastDescription.text =
                forecastData.getJSONObject(position).getJSONArray("weather").getJSONObject(0)
                    .getString("description")
            holder.forecastTemperature.text = String.format(
                "%.1f",
                forecastData.getJSONObject(position).getJSONObject("main").getString("temp")
                    .toDouble() - 273.15
            ) + " °C"
        }
    }

    private inner class ForecastDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var forecastDay: TextView = itemView.findViewById(R.id.txt_day)
        var dayImage: ImageView = itemView.findViewById(R.id.img_day)
        var forecastDescription: TextView = itemView.findViewById(R.id.txt_description)
        var forecastTemperature: TextView = itemView.findViewById(R.id.txt_temp)

        }
    }
}