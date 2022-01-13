package com.example.thebird.identity

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thebird.model.User
import com.example.thebird.repository.FirestoreService

class ProfileViewModel: ViewModel() {
    var mLiveData=MutableLiveData<Boolean>()

    fun uploadAvatar(avatar: Uri):MutableLiveData<Boolean>{
        FirestoreService.uploadAvatar(avatar)
            .addOnSuccessListener {
                mLiveData.postValue(true)
            }
            .addOnFailureListener {
                mLiveData.postValue(false)
            }
        return mLiveData
    }

    fun updateAvatar(avatar: String):LiveData<Boolean>{
        return FirestoreService.updateAvatar(avatar)
    }

    fun  getUserData():LiveData<User>{
        return FirestoreService.getUserData()
    }
}