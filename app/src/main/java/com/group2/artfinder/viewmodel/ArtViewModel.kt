package com.group2.artfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.artfinder.model.ArtworkItem
import com.group2.artfinder.data.ArtRepository
import kotlinx.coroutines.launch

class ArtViewModel : ViewModel() {

    private val repo = ArtRepository()

    private val _artworks = MutableLiveData<List<ArtworkItem>>(emptyList())
    val artworks: LiveData<List<ArtworkItem>> = _artworks

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadArtworks(page: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repo.getArtworks(page)
                _artworks.value = response.data ?: emptyList()
            } catch (e: Exception) {
                android.util.Log.e("ArtViewModel", "Error loading artworks", e)
                _artworks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchArtworks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repo.searchArtworks(query)
                _artworks.value = response.data ?: emptyList()
            } catch (e: Exception) {
                android.util.Log.e("ArtViewModel", "Error searching artworks", e)
                _artworks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}