package com.paul.weatherapp.data.repository

import com.paul.weatherapp.data.api.OpenWeatherMapApi
import com.paul.weatherapp.data.api.model.GeoCoderResponse
import com.paul.weatherapp.data.api.model.OpenWeatherMapResponse
import retrofit2.Response
import javax.inject.Inject

class OpenWeatherMapRepository @Inject constructor(
    private val openWeatherMapApi: OpenWeatherMapApi
) {

    suspend fun getLatLongFromZip(zip:String): Response<GeoCoderResponse> {
        return openWeatherMapApi.getLatLonByZip(zip)
    }


    suspend fun getWeatherByLatLon(lat: Double, lon: Double): Response<OpenWeatherMapResponse> {
        return openWeatherMapApi.getWeatherByLatLon(lat, lon)
    }
}