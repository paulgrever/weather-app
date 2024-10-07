package com.paul.weatherapp.ui.theme.composable

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paul.weatherapp.ui.theme.viewmodels.WeatherViewModel

@Composable
fun MainView() {
    val weatherViewModel  = viewModel(modelClass=WeatherViewModel::class.java)
    val state by weatherViewModel.state.collectAsState()


}