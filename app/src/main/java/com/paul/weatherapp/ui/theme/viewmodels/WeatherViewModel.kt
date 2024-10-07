package com.paul.weatherapp.ui.theme.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.paul.weatherapp.BuildConfig
import com.paul.weatherapp.data.api.model.GeoCoderResponse
import com.paul.weatherapp.data.api.model.OpenWeatherMapResponse
import com.paul.weatherapp.data.api.model.ResponseState
import com.paul.weatherapp.data.api.model.WeatherState
import com.paul.weatherapp.data.repository.OpenWeatherMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: OpenWeatherMapRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState?>
        get() = _state


    fun getWeatherByZipCode(zipcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = WeatherState(ResponseState.LOADING)
            val geoCoderResponse = repository.getLatLongFromZip(zipcode)
            if (geoCoderResponse.isSuccessful) {
                geoCoderResponse.body()?.let { geoCoder->
                    val weatherResults = repository.getWeatherByLatLon(
                        geoCoder.lat,
                        geoCoder.lon
                    )
                    if (weatherResults.isSuccessful) {
                        _state.value = WeatherState(ResponseState.SUCCESS, geoCoderResponse.body(), weatherResults.body())
                    } else {
                        _state.value = WeatherState(ResponseState.ERROR, geoCoderResponse.body(), null, weatherResults.errorBody().toString())
                    }
                }
            } else {
                _state.value = WeatherState(ResponseState.ERROR, null, null, geoCoderResponse.errorBody().toString())
            }

        }
    }
}