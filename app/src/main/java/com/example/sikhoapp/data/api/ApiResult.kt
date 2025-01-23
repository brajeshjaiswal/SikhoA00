package com.example.sikhoapp.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.Headers
import retrofit2.HttpException
import retrofit2.Response

sealed class ApiResult<T> {

    data class Success<T>(val data: T?, val headers: Headers? = null) : ApiResult<T>()

    data class Error<T>(val exception: Exception) : ApiResult<T>()

    data class Loading<T>(val data: T?) : ApiResult<T>()


}

fun <T> networkResult(response: suspend () -> Response<T>): Flow<ApiResult<T>> = flow {

    emit(ApiResult.Loading(null))

    val result = response()
    try {
        if (result.isSuccessful) {
            val body = result.body()
            if (body != null) {
                emit(ApiResult.Success(body))
            } else {
                emit(ApiResult.Success(null, headers = result.headers()))
            }
        } else {
            emit(ApiResult.Error(HttpException(result)))
        }
    } catch (e: Exception) {
        emit(ApiResult.Error(e))
    }
}
