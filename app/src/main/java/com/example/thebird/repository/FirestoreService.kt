package com.example.thebird.repository

import com.example.thebird.model.Post
import com.example.thebird.util.SharedPrefUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val POSTS_COLLECTION_NAME = "posts"
const val USERS_COLLECTION_NAME = "users"
object FirestoreService {
    private val firestore = Firebase.firestore
    private val postsCollection = firestore.collection(POSTS_COLLECTION_NAME)
    private val userCollection = firestore.collection(USERS_COLLECTION_NAME)
    private val userId = SharedPrefUtil.get().getUserId()

    //function to be used in viewmodel to add the post to the firestore.
    fun insertPost(post:Post)= postsCollection.document().set(post)
    fun getUsernameFromFirestore() = userCollection.document(userId!!).get()

}