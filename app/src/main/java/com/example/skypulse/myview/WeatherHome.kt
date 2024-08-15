package com.example.skypulse.myview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.skypulse.mymodel.NetworkResponse
import com.example.skypulse.mymodel.WeatherModel
import com.example.skypulse.myviewmodel.WeatherViewModel

@Composable
fun WeatherHome(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current





    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF50A7C2))
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search Box at the top
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                placeholder = { Text(text = "Search for any location", color = Color.Gray) },












                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            viewModel.getData(city)
                            keyboardController?.hide()
                        }
                        




                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.SansSerif
                ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Weather Details or Loading/Error Message
            when (val result = weatherResult.value) {
                is NetworkResponse.Error -> {
                    Text(
                        text = result.message,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
                NetworkResponse.Loading -> {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.White)
                }
                is NetworkResponse.Success -> {
                    WeatherDetails(weatherData = result.data)
                }
                null -> {}
            }
        }
    }
}

@Composable
fun WeatherDetails(weatherData: WeatherModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
        //backgroundColor = Color.White.copy(alpha = 0.8f),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF50A7C2)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${weatherData.location.name}, ${weatherData.location.country}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A90E2),
                    fontFamily = FontFamily.Serif
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "${weatherData.current.temp_c}°",
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF4A90E2),
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                modifier = Modifier.size(120.dp),
                model = "https:${weatherData.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = "Condition Icon"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weatherData.current.condition.text,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF50A7C2),
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal(key = "Feels Like", value = "${weatherData.current.feelslike_c}°")
                WeatherKeyVal(key = "Wind Speed", value = "${weatherData.current.wind_kph} km/h")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal(key = "Humidity", value = "${weatherData.current.humidity}%")
                WeatherKeyVal(key = "Dew Point", value = "${weatherData.current.dewpoint_c}°")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal(key = "Visibility", value = "${weatherData.current.vis_km} km")
                WeatherKeyVal(key = "UV Index", value = "${weatherData.current.uv}")
            }
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = key,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4A90E2),
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black
        )
    }
}

