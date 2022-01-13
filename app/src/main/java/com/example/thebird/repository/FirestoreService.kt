package com.example.thebird.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thebird.model.Post
import com.example.thebird.model.User
import com.example.thebird.util.SharedPrefUtil

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.Query

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val POSTS_COLLECTION_NAME = "posts"
const val USERS_COLLECTION_NAME = "users"

const val FAVORITES_COLLECTION_NAME = "favorites"
const val TIMESTAMP_FIELD = "timestamp"


object FirestoreService {
    private val firestore = Firebase.firestore
    private val userId = SharedPrefUtil.get().getUserId()
    private val postsCollection = firestore.collection(POSTS_COLLECTION_NAME)
    private val userCollection = firestore.collection(USERS_COLLECTION_NAME)

    val db = Firebase.firestore
    //val uid = FirebaseAuth.getInstance().currentUser!!.uid


    private val favoritesCollectionForUser =
        firestore.collection(FAVORITES_COLLECTION_NAME).document(userId!!).collection(
            FAVORITES_COLLECTION_NAME
        )


    //function to be used in viewmodel to add the post to the firestore.
    fun insertPost(post: Post) = postsCollection.document().set(post)
    fun getUsernameFromFirestore() = userCollection.document(userId!!).get()
    fun uploadAvatar(avatar: Uri) = userCollection.document(userId!!).update("avatar", avatar)

    fun updateAvatar(avatar: String): LiveData<Boolean> {
        var mLiveData = MutableLiveData<Boolean>()
        db.collection("users").document(userId!!)
            .update("avatar", avatar)
            .addOnSuccessListener {
                mLiveData.postValue(true)
            }
            .addOnFailureListener {
                mLiveData.postValue(false)
            }
        return mLiveData
    }

    fun getUserData(): LiveData<User> {
        var mLiveData = MutableLiveData<User>()
        var auth = Firebase.auth
        db.collection("users").document(userId!!)
            .addSnapshotListener { dr, message ->
                if (dr != null) {
                    mLiveData.postValue(
                        User(
                            dr.getString("firstname").toString(),
                            dr.getString("username").toString(),
                            dr.getString("email").toString(),
                            dr.getString("avatar").toString()
                        )
                    )
                }

            }
        return mLiveData
    }

    //fun getPosts() = postsCollection.orderBy(TIMESTAMP_FIELD,Query.Direction.DESCENDING) .get()
    fun getPosts() = postsCollection.orderBy(TIMESTAMP_FIELD, Query.Direction.DESCENDING)
    fun updateFavorite(post: Post) = favoritesCollectionForUser.document(post.id!!).set(post)

    fun deleteFavorite(post: Post) = favoritesCollectionForUser.document(post.id!!).delete()

    fun getAllFavoritesForUser() = favoritesCollectionForUser


}