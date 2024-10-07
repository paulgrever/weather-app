package com.paul.weatherapp.data.api

import com.paul.weatherapp.data.api.model.GeoCoderResponse
import com.paul.weatherapp.data.api.model.OpenWeatherMapResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherMapApi {

    @GET(EndpointConstants.FORECAST)
    suspend fun getWeatherByLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
        ): Response<OpenWeatherMapResponse>

    @GET(EndpointConstants.GEOCODER)
    suspend fun getLatLonByZip(
        @Query("zip") zipCode: String,
//        @Query("appid") apiKey: String= BuildConfig.M
    ): Response<GeoCoderResponse>

}