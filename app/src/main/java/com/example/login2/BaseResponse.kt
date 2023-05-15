package com.example.login2


    sealed class BaseResponse<out T> {
        data class Success<out T>(val data: T? = null) : BaseResponse<T>()
        data class Loading(val nothing: Nothing? = null) : BaseResponse<Nothing>()
        data class Error(val msg: String?) : BaseResponse<Nothing>()
    }

