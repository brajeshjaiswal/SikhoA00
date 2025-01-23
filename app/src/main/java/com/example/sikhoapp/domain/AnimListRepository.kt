package com.example.sikhoapp.domain

import com.example.sikhoapp.data.api.ApiResult
import com.example.sikhoapp.model.AnimDetailsResponse
import com.example.sikhoapp.model.AnimeListResponse
import kotlinx.coroutines.flow.Flow

interface AnimListRepository {

    suspend fun getAnimeList(): Flow<ApiResult<AnimeListResponse>>

    suspend fun getAnimeDetail(id: Int?): Flow<ApiResult<AnimDetailsResponse>>
}