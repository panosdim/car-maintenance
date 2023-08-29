package com.panosdim.carmaintenance.model

sealed class Response<out T> {
    class Success<out T>(val data: T) : Response<T>()
    data object Loading : Response<Nothing>()
    class Error(val errorMessage: String) : Response<Nothing>()
}