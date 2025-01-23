package com.example.sikhoapp.model

data class AnimeListResponse(
    val data: List<AnimeData>? = null
) {
    data class AnimeData(
        val mal_id: String? = null,
        val images: Images? = null,
        val trailer: Trailer? = null,
        val title: String? = null,
        val title_english: String? = null,
        val episodes: String? = null,
        val rating: String? = null,
        val score: String? = null,
        val synopsis: String? = null,
        val genres: List<Genere>? = null,
    ) {
        data class Images(
            val webp: WebP? = null,
            val large_image_url: String? = null,
            val image_url: String? = null,
            val small_image_url: String? = null,
            val maximum_image_url: String? = null,
            val medium_image_url: String? = null,
        ) {
            data class WebP(
                val large_image_url: String? = null,
                val image_url: String? = null,
                val small_image_url: String? = null,

                ) {}
        }

        data class Trailer(
            val youtube_id: String? = null,
            val url: String? = null,
            val embed_url: String? = null,
            val images: Images? = null,
        ) {

        }

        data class Genere(val name: String? = null)

    }
}
