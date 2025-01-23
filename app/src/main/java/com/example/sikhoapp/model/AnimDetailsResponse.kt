package com.example.sikhoapp.model

import com.google.gson.annotations.SerializedName

data class AnimDetailsResponse(
    @SerializedName("data")
    val data: AnimeListResponse.AnimeData? = null
)
