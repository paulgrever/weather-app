package com.paul.weatherapp.data.api.model

enum class ResponseState {
    IN_ACTIVE,
    LOADING,
    SUCCESS,
    ERROR
}

data class WeatherState(
    var responseState: ResponseState = ResponseState.IN_ACTIVE,
    var geoCoderResponse: GeoCoderResponse? = null,
    var weatherResponse: OpenWeatherMapResponse? = null,
    var error: String? = null

)