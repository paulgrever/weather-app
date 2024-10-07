package com.paul.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paul.weatherapp.business.DateTimeHelper
import com.paul.weatherapp.data.api.model.CurrentWeather
import com.paul.weatherapp.data.api.model.DailyWeather
import com.paul.weatherapp.data.api.model.ResponseState
import com.paul.weatherapp.ui.theme.WeatherAppTheme
import com.paul.weatherapp.ui.theme.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val weatherViewModel = viewModel(modelClass = WeatherViewModel::class.java)

                    HomeScreen(
                        modifier = Modifier.padding(innerPadding), weatherViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun HomeScreen(modifier: Modifier = Modifier, weatherViewModel: WeatherViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by weatherViewModel.state.collectAsState()
    val city = state?.geoCoderResponse?.name ?: ""
    var zipCode by remember {
        mutableStateOf("")
    }
    var isError by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.weather_app),
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .padding(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .height(IntrinsicSize.Min)

        ) {
            OutlinedTextField(
                value = zipCode,
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                placeholder = {
                    Text(text = stringResource(R.string.add_zipcode))
                },
                label = {
                    Text(text = stringResource(R.string.zipcode))
                },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(text = stringResource(R.string.invalid_zipcode))
                    }
                },
                onValueChange = { newZipCode ->
                zipCode = newZipCode
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .padding(0.dp, 8.dp, 0.dp, 16.dp)
                    .fillMaxHeight(),
                onClick = {
                if (zipCode.length == 5) {
                    isError = false
                    weatherViewModel.getWeatherByZipCode(zipCode)
                    keyboardController?.hide()
                } else {
                    isError = true
                }
            }) {
                Text(text = stringResource(R.string.get_weather))
            }

        }
        if (city.isNotEmpty()) {
            Text(
                text = stringResource(R.string.weather_in, city, zipCode),
                color = Color.DarkGray,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        when (state?.responseState) {
            ResponseState.LOADING -> {
                Text(text = stringResource(R.string.loading),
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                )
            }
            ResponseState.SUCCESS -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                ) {
                    item {
                        CurrentWeatherBlock(stringResource(R.string.current_weather), state?.weatherResponse?.current, modifier = Modifier)

                    }
                    items(state?.weatherResponse?.daily ?: emptyList()) { weatherItem ->
                        Column {
                            val formattedDateTime = DateTimeHelper().getFriendlyDate(weatherItem.dt)
                            DailyWeatherBlock(formattedDateTime, weatherItem, modifier = Modifier)
                        }
                    }
                }
            }

            ResponseState.ERROR -> {
                Text(text = stringResource(R.string.error_getting_the_weather_for_your_area))
            }

            else -> {}
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WeatherAppTheme {
        HomeScreen(modifier = Modifier, weatherViewModel = viewModel())
    }
}

@Composable
fun CurrentWeatherBlock(headerText: String, dailyWeather: CurrentWeather?, modifier: Modifier = Modifier) {
    WeatherHeader(headerText = headerText)
    WeatherBody("Temperature ${dailyWeather?.temp.toString()}F")
    WeatherBody("Wind ${dailyWeather?.wind_speed.toString()} mph ${dailyWeather?.wind_deg.toString()} deg")
    WeatherBody("${dailyWeather?.weather?.get(0)?.description}")
    WeatherBody("Humidity ${dailyWeather?.humidity.toString()}%")
    WeatherBody("Pressure ${dailyWeather?.pressure.toString()} hPa")
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun DailyWeatherBlock(headerText: String, dailyWeather: DailyWeather?, modifier: Modifier = Modifier) {
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
