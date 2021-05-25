package com.nurrizkiadip_a1201541.moviecatalogue.core.data

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}