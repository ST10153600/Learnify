package com.learnify.learnify.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.learnify.learnify.services.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<FirebaseUser?>(FirebaseService.auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    init {
        FirebaseService.auth.addAuthStateListener { auth ->
            viewModelScope.launch {
                _currentUser.emit(auth.currentUser)
            }
        }
    }

    fun logout() {
        FirebaseService.auth.signOut()
    }
}
