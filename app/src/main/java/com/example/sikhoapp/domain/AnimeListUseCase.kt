package com.example.sikhoapp.domain

import com.example.sikhoapp.data.api.ApiResult
import com.example.sikhoapp.model.AnimeListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeListUseCase @Inject constructor(val repository: AnimListRepository) {

    suspend fun invokeApi(): Flow<ApiResult<AnimeListResponse>> {
        return repository.getAnimeList()
    }
}