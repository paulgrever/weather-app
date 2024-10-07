package com.paul.weatherapp.data.api

object EndpointConstants {
    const val BASE_URL = "https://api.openweathermap.org/"
    const val GEOCODER = "geo/1.0/zip"
    const val FORECAST = "data/3.0/onecall?exclude=minutely,hourly&mode=json&units=imperial"
}
