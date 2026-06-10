package com.group2.artfinder.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.group2.artfinder.repository.UserRepository

class AuthViewModel : ViewModel() {
    private val repo = UserRepository()

    val authResult = MutableLiveData<Boolean>()

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            authResult.value = repo.register(email, password, name)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authResult.value = repo.login(email, password)
        }
    }

    fun isLoggedIn() = repo.isLoggedIn()
}