package com.group2.artfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.artfinder.model.User
import com.group2.artfinder.data.UpdateResult
import com.group2.artfinder.data.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _updateResult = MutableLiveData<UpdateResult?>()
    val updateResult: LiveData<UpdateResult?> = _updateResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _user.value = repo.getUserProfile()
            _isLoading.value = false
        }
    }

    fun updateProfile(firstName: String, lastName: String, username: String, dob: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _updateResult.value = null
            _updateResult.value = repo.updateProfile(firstName, lastName, username, dob)
            if (_updateResult.value == UpdateResult.Success) loadProfile()
            _isLoading.value = false
        }
    }

    fun clearUpdateResult() {
        _updateResult.value = null
    }

    fun logout() = repo.logout()
}