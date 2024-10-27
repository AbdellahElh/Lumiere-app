package com.example.riseandroid.repository


sealed class APIResource<T>(val data: T? = null, val message: String? = null) {

    class Loading<T>(data: T? = null) : APIResource<T>(data)
    class Success<T>(data: T?) : APIResource<T>(data)
    class Error<T>(message: String, data: T? = null) : APIResource<T>(data, message)

}