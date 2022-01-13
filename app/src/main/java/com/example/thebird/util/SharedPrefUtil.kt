package com.example.thebird.util

import android.content.Context
import android.content.SharedPreferences
import java.lang.Exception


private const val SHARED_PREF_FILE = "auth"
private const val USERNAME_KEY = "username"
private const val USER_ID_KEY = "user_Id"

//a class to be used for sharedPreferences in all the application.
class SharedPrefUtil(context: Context) {
    private var sharedPref = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
    private var sharedPrefEditor = sharedPref.edit()

    // a function to get the username of the user from the sharedPreferences.
    fun getUsername(): String? {
        return sharedPref.getString(USERNAME_KEY, "")
    }

    //a function to set the username of the user.
    fun setUsername(username: String?) {

        //save the username in the sharedPreferences.
        sharedPrefEditor.putString(USERNAME_KEY, username).commit()
    }

    // a function to set the userID of the logged in user in the sharedPreferences.
    fun setUserID(userID: String?) {
        sharedPrefEditor.putString(USER_ID_KEY, userID).commit()
    }

    //a function to retrieve the userID of the logged user from the sharedPreferences.
    fun getUserId(): String? {
        return sharedPref.getString(USER_ID_KEY, "")
    }


    companion object {
        private var INSTANCE: SharedPrefUtil? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SharedPrefUtil(context)
            }
        }

        fun get(): SharedPrefUtil {
            return INSTANCE ?: throw Exception("INSTANCE of SharedPrefUtil is NULL!!")
        }
    }
}