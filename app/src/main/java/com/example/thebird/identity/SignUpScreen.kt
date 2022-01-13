package com.example.thebird.identity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.thebird.R
import com.example.thebird.repository.FirestoreService
import com.example.thebird.repository.USERS_COLLECTION_NAME
import com.example.thebird.util.SharedPrefUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpScreen : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_sign_up_screen, container, false)
        var fnameEditText = v.findViewById<EditText>(R.id.editTextFirstName)
        var unameEditText = v.findViewById<EditText>(R.id.editTextUserName)
        var signUpEmailEditText = v.findViewById<EditText>(R.id.editTextSignUpEmail)
        var passwordEditText = v.findViewById<EditText>(R.id.editTextTextPassword)
        var signUpButton = v.findViewById<Button>(R.id.buttonSignUp)
        var loginTextView = v.findViewById<TextView>(R.id.textViewLogin)
        var auth = Firebase.auth


        loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signUpScreen_to_loginScreen)
        }

        signUpButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                signUpEmailEditText.text.toString(),
                passwordEditText.text.toString()
            )
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        println("User has been registered successfully with UID " + auth.currentUser?.uid)
                        SharedPrefUtil.get().setUserID(auth.currentUser?.uid)


                        val u = hashMapOf(
                            "email" to auth.currentUser?.email,
                            "firstname" to fnameEditText.text.toString(),
                            "username" to unameEditText.text.toString()
                        )

                        var db = Firebase.firestore


                        db.collection(USERS_COLLECTION_NAME).document(auth.currentUser?.uid.toString())
                            .set(u).addOnSuccessListener {
                                SharedPrefUtil.get().setUsername(unameEditText.text.toString())
                                findNavController().navigate(R.id.action_signUpScreen_to_timelineScreen)
                            }
                    }


                }


        }
        return v
    }

}