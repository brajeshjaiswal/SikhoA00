package com.example.sikhoapp.domain

import com.example.sikhoapp.data.api.ApiResult
import com.example.sikhoapp.model.AnimDetailsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeDetailUseCase @Inject constructor(private val animListRepository: AnimListRepository) {

    suspend fun invokeApi(animId: Int?): Flow<ApiResult<AnimDetailsResponse>> {
        return animListRepository.getAnimeDetail(animId)

    }
}