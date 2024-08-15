package com.example.skypulse.myviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skypulse.mymodel.Api
import com.example.skypulse.mymodel.NetworkResponse
import com.example.skypulse.mymodel.RetrofitInstance
import com.example.skypulse.mymodel.WeatherApi
import com.example.skypulse.mymodel.WeatherModel
import kotlinx.coroutines.launch


class WeatherViewModel : ViewModel() {

    private val weatherApi  = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {

            val response = weatherApi.getWeather(Api.api, city)


            try{
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }

                } else
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }
            catch (e: Exception){
                _weatherResult.value = NetworkResponse.Error("Data loading failed")
            }
        }

    }

}