package com.example.thebird.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebird.model.Post
import com.example.thebird.repository.FirestoreService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "TimelineViewModel"

class TimelineViewModel : ViewModel() {

    val postsListMutableLiveData = MutableLiveData<List<Post>>()
    val errorPostsList = MutableLiveData<String>()
    val postsList = mutableListOf<Post>()
    val favoriteList = mutableListOf<Post>()
    val errorFavoritesList = MutableLiveData<String>()
    val favoriteListMutableLiveData = MutableLiveData<List<Post>>()

    // a function to retrieve all posts
//    fun getPosts() {
//        viewModelScope.launch(Dispatchers.IO)
//        {
//            FirestoreService.getPosts().addOnCompleteListener { task ->
//
//                if (task.isSuccessful) {
//                    postsList.clear()
//                    Log.d(TAG, "${task.result?.documents}")
//                    val documents = task.result?.documents
//                    documents?.forEach {
//                        val postText = it.get("postText").toString()
//                        val timestamp = it.get("timestamp").toString().toLong()
//                        val username = it.get("username").toString()
////                        Log.d(TAG, "${it.data}")
////                        Log.d(
////                            TAG,
////                            "${Gson().fromJson(it.data.toString(), Post::class.java)}"
////                        )
//////                        val postObject =
//////                            Gson().fromJson(it.data.toString(), Post::class.java)
////                        it.toObject(Post::class.java)?.let { post -> postsList.add(post) }
//                        postsList.add(Post(postText,username,timestamp))
//                    }
//                    postsListMutableLiveData.postValue(postsList)
//                } else {
//                    errorPostsList.postValue(task.exception?.message.toString())
//                }
//            }
//        }
//    }

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            FirestoreService.getPosts().addSnapshotListener { querySnapshot, e ->

                if (e != null) {
                    Log.d(TAG, "There is an error while listening $e")
                    errorPostsList.postValue(e.message)
                }
                if (!querySnapshot!!.isEmpty) {
                    postsList.clear()
                    val documents = querySnapshot.documents
                    documents.forEach {
                        Log.d(TAG, "${it.data}")
                        val postText = it.get("postText").toString()
                        val timestamp = it.get("timestamp").toString().toLong()
                        val username = it.get("username").toString()
                        val id = it.id
                        Log.d(TAG, it.id)
                        postsList.add(Post(id, postText, username, timestamp))
                    }
                    postsListMutableLiveData.postValue(postsList)
                    Log.d(TAG, "${querySnapshot.documents}")
                }


            }
        }
    }

    fun addToFavorites(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            FirestoreService.updateFavorite(post).addOnSuccessListener {
                Log.d(TAG, "Added successfully to favorites.")
            }.addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }
        }
    }

    fun removeFromFavorites(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            FirestoreService.deleteFavorite(post).addOnSuccessListener {
                Log.d(TAG, "Added successfully to favorites.")
            }.addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }
        }
    }

    fun getAllFavoritesForUser() {
        Log.d(TAG,"GetAllFavorites function is called!")
        FirestoreService.getAllFavoritesForUser().addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.d(TAG, "There is an error while listening $e")
                errorFavoritesList.postValue(e.message)
            }
            if (!querySnapshot!!.isEmpty) {
                favoriteList.clear()
                val documents = querySnapshot.documents
                Log.d(TAG,"Inside the get favorites function, inside the if statement")
                documents.forEach {
                    Log.d(TAG, "${it.data}")
                    val postText = it.get("postText").toString()
                    val timestamp = it.get("timestamp").toString().toLong()
                    val username = it.get("username").toString()
                    val id = it.id
                    Log.d(TAG, it.id)
                    favoriteList.add(Post(id, postText, username, timestamp))
                }

                Log.d(TAG, "${querySnapshot.documents}")
            }
            favoriteListMutableLiveData.postValue(favoriteList)
        }
    }
}