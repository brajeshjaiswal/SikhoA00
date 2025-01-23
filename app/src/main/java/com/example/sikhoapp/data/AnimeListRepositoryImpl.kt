package com.example.sikhoapp.data

import com.example.sikhoapp.data.api.AnimApi
import com.example.sikhoapp.data.api.ApiResult
import com.example.sikhoapp.data.api.networkResult
import com.example.sikhoapp.domain.AnimListRepository
import com.example.sikhoapp.model.AnimDetailsResponse
import com.example.sikhoapp.model.AnimeListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeListRepositoryImpl @Inject constructor(private val apiService: AnimApi) : AnimListRepository {

    override suspend fun getAnimeList(): Flow<ApiResult<AnimeListResponse>> {

       return networkResult {
           apiService.getAnimeList()
       }
    }

    override suspend fun getAnimeDetail(id: Int?): Flow<ApiResult<AnimDetailsResponse>> {
        return networkResult {
            apiService.getAnimeDetails(id)
        }
    }
}