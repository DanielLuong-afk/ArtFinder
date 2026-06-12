package com.group2.artfinder.data.api

import com.group2.artfinder.data.model.ArtworkListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtApiService {

    @GET("artworks")
    suspend fun getArtworks(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String = "id,title,artist_display,artwork_type_title,image_id,place_of_origin,gallery_title,date_display,medium_display,latitude,longitude"
    ): ArtworkListResponse

    @GET("artworks/search")
    suspend fun searchArtworks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("fields") fields: String = "id,title,artist_display,artwork_type_title,image_id,place_of_origin,gallery_title,date_display,medium_display,latitude,longitude"
    ): ArtworkListResponse
}