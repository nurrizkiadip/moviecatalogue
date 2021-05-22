package com.nurrizkiadip_a1201541.moviecatalogue.vo

sealed class Resource <T> (val data: T?, val message: String?)
class SuccessResource<T>(data: T?): Resource<T>(data, null)
class EmptyResource<T>(message: String?): Resource<T>(null, message)
class LoadingResource<T>(data: T?): Resource<T>(data, null)
class ErrorResource<T>(data: T?, message: String?): Resource<T>(data, message)
