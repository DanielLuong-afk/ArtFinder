package com.group2.artfinder.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.artfinder.data.VisitedArtworkRepository
import com.group2.artfinder.model.ArtworkItem
import com.group2.artfinder.model.VisitedArtwork
import kotlinx.coroutines.launch

class VisitedArtworkViewModel : ViewModel() {
    private val repo = VisitedArtworkRepository()

    private val _visitedArtworks = MutableLiveData<List<VisitedArtwork>>(emptyList())
    val visitedArtworks: LiveData<List<VisitedArtwork>> = _visitedArtworks

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isVisited = MutableLiveData(false)
    val isVisited: LiveData<Boolean> = _isVisited

    private val _photos = MutableLiveData<List<String>>(emptyList())
    val photos: LiveData<List<String>> = _photos

    private val _uploadLoading = MutableLiveData(false)
    val uploadLoading: LiveData<Boolean> = _uploadLoading

    fun loadVisitedArtworks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _visitedArtworks.value = repo.getVisitedArtworks()
            } catch (e: Exception) {
                _visitedArtworks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addVisitedArtwork(artwork: ArtworkItem) {
        viewModelScope.launch {
            try {
                repo.addVisitedArtwork(artwork)
                checkIfVisited(artwork.id)
            } catch (e: Exception) {
                android.util.Log.e("VisitedVM", "Error adding", e)
            }
        }
    }

    fun deleteVisitedArtwork(artworkId: Int) {
        viewModelScope.launch {
            try {
                repo.deleteVisitedArtwork(artworkId)
                loadVisitedArtworks()
            } catch (e: Exception) {
                android.util.Log.e("VisitedVM", "Error deleting", e)
            }
        }
    }

    fun checkIfVisited(artworkId: Int) {
        viewModelScope.launch {
            try {
                _isVisited.value = repo.isVisited(artworkId)
            } catch (e: Exception) {
                _isVisited.value = false
            }
        }
    }

    fun loadPhotos(artworkId: Int) {
        viewModelScope.launch {
            try {
                _photos.value = repo.getPhotos(artworkId)
            } catch (e: Exception) {
                _photos.value = emptyList()
            }
        }
    }

    fun uploadPhoto(context: Context, artworkId: Int, uri: Uri, onDone: () -> Unit) {
        viewModelScope.launch {
            _uploadLoading.value = true
            try {
                repo.savePhotoAndUpdatePoints(context, artworkId, uri)
                _photos.value = repo.getPhotos(artworkId)
            } catch (e: Exception) {
                android.util.Log.e("VisitedVM", "Error uploading photo", e)
            } finally {
                _uploadLoading.value = false
                onDone()
            }
        }
    }
}