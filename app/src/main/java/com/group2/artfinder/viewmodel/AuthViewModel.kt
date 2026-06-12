package com.group2.artfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.artfinder.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _authResult = MutableLiveData<Boolean?>()
    val authResult: LiveData<Boolean?> = _authResult

    fun register(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = repo.register(email, password, firstName, lastName)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = repo.login(email, password)
        }
    }

    fun isLoggedIn() = repo.isLoggedIn()
}