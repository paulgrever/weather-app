package com.paul.weatherapp.data.api.model

data class GeoCoderResponse(
    val zip: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)