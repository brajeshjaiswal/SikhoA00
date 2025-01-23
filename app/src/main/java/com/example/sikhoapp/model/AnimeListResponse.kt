package com.example.sikhoapp.model

import com.google.gson.annotations.SerializedName

data class AnimeListResponse(
    @SerializedName("data")
    val data: List<AnimeData>? = null
) {
    data class AnimeData(
        @SerializedName("mal_id")
        val id: String? = null,
        @SerializedName("images")
        val images: Images? = null,
        @SerializedName("trailer")
        val trailer: Trailer? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("title_english")
        val titleEnglish: String? = null,
        @SerializedName("episodes")
        val episodes: String? = null,
        @SerializedName("rating")
        val rating: String? = null,
        @SerializedName("score")
        val score: String? = null,
        @SerializedName("synopsis")
        val synopsis: String? = null,
        @SerializedName("genres")
        val genres: List<Genere>? = null,
    ) {
        data class Images(
            @SerializedName("webp")
            val webp: WebP? = null,
            @SerializedName("large_image_url")
            val largeImageUrl: String? = null,
            @SerializedName("image_url")
            val imageUrl: String? = null,
            @SerializedName("small_image_url")
            val smallImageUrl: String? = null,
            @SerializedName("maximum_image_url")
            val maximumImageUrl: String? = null,
            @SerializedName("medium_image_url")
            val mediumImageUrl: String? = null,
        ) {
            data class WebP(
                @SerializedName("large_image_url")
                val largeImageUrl: String? = null,
                @SerializedName("image_url")
                val imageUrl: String? = null,
                @SerializedName("small_image_url")
                val smallImageUrl: String? = null,

                )
        }

        data class Trailer(
            @SerializedName("youtube_id")
            val youtubeId: String? = null,
            @SerializedName("url")
            val url: String? = null,
            @SerializedName("embed_url")
            val embedUrl: String? = null,
            @SerializedName("images")
            val images: Images? = null,
        ) {

        }

        data class Genere(
            @SerializedName("name")
            val name: String? = null
        )

    }
}
