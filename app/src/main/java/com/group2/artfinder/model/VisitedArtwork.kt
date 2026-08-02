package com.group2.artfinder.model

import com.google.firebase.Timestamp

data class VisitedArtwork(
    val id: Int = 0,
    val title: String = "",
    val artist_display: String? = null,
    val artwork_type_title: String? = null,
    val image_id: String? = null,
    val gallery_title: String? = null,
    val date_display: String? = null,
    val place_of_origin: String? = null,
    val medium_display: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val visitedAt: Timestamp? = null,
    val photos: List<String> = emptyList(),
    val pointsAwarded: Int = 0
)