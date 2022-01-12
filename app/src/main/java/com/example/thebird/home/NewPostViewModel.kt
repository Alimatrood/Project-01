package com.example.thebird.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebird.model.Post
import com.example.thebird.repository.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPostViewModel : ViewModel() {

    val successfullyAddingPost = MutableLiveData<String>()
    val errorAddingPost = MutableLiveData<String>()

    //function to add the post to the firestore.
    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            FirestoreService.insertPost(post).addOnSuccessListener {
                successfullyAddingPost.postValue("Posted Successfully!")
            }.addOnFailureListener {
                errorAddingPost.postValue("Error while posting! Please try again later.")
            }
        }
    }
}