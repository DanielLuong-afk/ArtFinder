package com.group2.artfinder.repository

import com.group2.artfinder.data.api.RetrofitInstance

class ArtRepository {
    private val api = RetrofitInstance.api

    suspend fun getArtworks(page: Int = 1) = api.getArtworks(page = page)

    suspend fun searchArtworks(query: String) = api.searchArtworks(query)
}