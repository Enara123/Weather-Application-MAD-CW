package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(private val cities: List<City>) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(city: City) {
            val cityNameTextView: TextView = itemView.findViewById(R.id.txt_cityName)
            cityNameTextView.text = city.name
        }
    }
}
