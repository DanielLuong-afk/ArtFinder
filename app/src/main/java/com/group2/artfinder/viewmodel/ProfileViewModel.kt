package com.group2.artfinder.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.artfinder.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repo = UserRepository()

    val profile = MutableLiveData<Map<String, Any>?>()
    val updateResult = MutableLiveData<Boolean>()

    fun loadProfile() {
        viewModelScope.launch {
            profile.value = repo.getUserProfile()
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            updateResult.value = repo.updateUserProfile(name)
            if (updateResult.value == true) loadProfile()
        }
    }

    fun logout() = repo.logout()
}