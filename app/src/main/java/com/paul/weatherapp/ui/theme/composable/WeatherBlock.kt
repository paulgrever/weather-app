package com.paul.weatherapp.ui.theme.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paul.weatherapp.data.api.model.CurrentWeather
import com.paul.weatherapp.data.api.model.DailyWeather

@Composable
fun CurrentWeatherBlock(
    headerText: String,
    dailyWeather: CurrentWeather?,
    modifier: Modifier = Modifier
) {
    WeatherHeader(headerText = headerText)
    WeatherBody("Temperature ${dailyWeather?.temp.toString()}F")
    WeatherBody("Wind ${dailyWeather?.wind_speed.toString()} mph ${dailyWeather?.wind_deg.toString()} deg")
    WeatherBody("${dailyWeather?.weather?.get(0)?.description}")
    WeatherBody("Humidity ${dailyWeather?.humidity.toString()}%")
    WeatherBody("Pressure ${dailyWeather?.pressure.toString()} hPa")
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun DailyWeatherBlock(
    headerText: String,
    dailyWeather: DailyWeather?,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 16.dp)// Smoothly animate the change in content size
    ) {
        WeatherHeader(
            headerText = headerText,
            modifier = Modifier

        )
        WeatherBody("Temperature ${dailyWeather?.temp?.day.toString()}F")
    }
    AnimatedVisibility(visible = isExpanded) {
        Column {
            WeatherBody("${dailyWeather?.summary}")
            WeatherBody("Wind ${dailyWeather?.wind_speed.toString()} mph ${dailyWeather?.wind_deg.toString()} deg")
            WeatherBody("Humidity ${dailyWeather?.humidity.toString()}%")
            WeatherBody("Pressure ${dailyWeather?.pressure.toString()} hPa")

        }
    }
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun WeatherHeader(headerText: String, modifier: Modifier = Modifier) {
    Text(
        text = headerText,
        color = Color.DarkGray,
        fontSize = 16.sp,
        modifier = modifier
    )
}

@Composable
fun WeatherBody(bodyText: String, modifier: Modifier = Modifier) {
    Text(
        text = bodyText,
        color = Color.Gray,
        fontSize = 14.sp,
        modifier = modifier

    )
}