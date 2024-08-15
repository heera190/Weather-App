package com.example.skypulse.mymodel

sealed class NetworkResponse<out T> {

    data class Success<T>(val data : T) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}