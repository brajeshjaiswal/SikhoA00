package com.example.sikhoapp.data.api

import com.example.sikhoapp.model.AnimDetailsResponse
import com.example.sikhoapp.model.AnimeListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimApi {

    @GET("top/anime")
    suspend fun getAnimeList() : Response<AnimeListResponse>

    @GET("anime/{animId}")
    suspend fun getAnimeDetails(@Path("animId") animId: Int?): Response<AnimDetailsResponse>
}