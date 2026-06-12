package com.group2.artfinder.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.artfinder.data.model.ArtworkItem
import com.group2.artfinder.repository.ArtRepository
import kotlinx.coroutines.launch

class ArtViewModel : ViewModel() {
    private val repo = ArtRepository()

    val artworks = MutableLiveData<List<ArtworkItem>>()
    val isLoading = MutableLiveData<Boolean>()

    fun loadArtworks(page: Int = 1) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repo.getArtworks(page)
                artworks.value = response.data ?: emptyList()
            } catch (e: Exception) {
                android.util.Log.e("ArtViewModel", "Error loading artworks", e)
                artworks.value = emptyList()
            }
            isLoading.value = false
        }
    }

    fun searchArtworks(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = repo.searchArtworks(query)
                artworks.value = response.data ?: emptyList()
            } catch (e: Exception) {
                artworks.value = emptyList()
            }
            isLoading.value = false
        }
    }
}