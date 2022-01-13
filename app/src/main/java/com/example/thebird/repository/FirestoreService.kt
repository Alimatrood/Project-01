package com.example.thebird.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thebird.model.Post
import com.example.thebird.model.User
import com.example.thebird.util.SharedPrefUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val POSTS_COLLECTION_NAME = "posts"
const val USERS_COLLECTION_NAME = "users"

object FirestoreService {
    private val firestore = Firebase.firestore
    private val postsCollection = firestore.collection(POSTS_COLLECTION_NAME)
    private val userCollection = firestore.collection(USERS_COLLECTION_NAME)
    private val userId = SharedPrefUtil.get().getUserId()
    val db = Firebase.firestore
    val uid = FirebaseAuth.getInstance().currentUser!!.uid


    //function to be used in viewmodel to add the post to the firestore.
    fun insertPost(post: Post) = postsCollection.document().set(post)
    fun getUsernameFromFirestore() = userCollection.document(userId!!).get()
    fun uploadAvatar(avatar: Uri) = userCollection.document(userId!!).update("avatar", avatar)

    fun updateAvatar(avatar: String): LiveData<Boolean> {
        var mLiveData = MutableLiveData<Boolean>()
        db.collection("users").document(uid)
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
        db.collection("users").document(uid)
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

}