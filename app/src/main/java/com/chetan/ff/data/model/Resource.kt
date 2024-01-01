package com.chetan.ff.data.model

import java.lang.Exception

sealed class Resource<out T>{
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
