package com.example.thebird.identity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebird.repository.FirestoreService
import com.example.thebird.util.SharedPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    fun getUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            FirestoreService.getUsernameFromFirestore().addOnSuccessListener {
                val username = it.get("username").toString()
                SharedPrefUtil.get().setUsername(username)
            }
        }
    }
}