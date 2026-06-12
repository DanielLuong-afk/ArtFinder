package com.group2.artfinder.model

data class ArtworkListResponse(
    val data: List<ArtworkItem>,
    val pagination: Pagination
)

data class Pagination(
    val total: Int,
    val limit: Int,
    val offset: Int,
    val current_page: Int,
    val total_pages: Int
)

data class ArtworkItem(
    val id: Int,
    val title: String,
    val artist_display: String?,
    val artwork_type_title: String?,
    val image_id: String?,
    val place_of_origin: String?,
    val gallery_title: String?,
    val date_display: String?,
    val medium_display: String?,
    val latitude: Double?,
    val longitude: Double?
)