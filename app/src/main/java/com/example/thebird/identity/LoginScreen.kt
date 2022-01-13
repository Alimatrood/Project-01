package com.example.thebird.identity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.thebird.R
import com.example.thebird.databinding.FragmentLoginScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.thebird.util.SharedPrefUtil

private const val TAG = "LoginScreen"
class LoginScreen : Fragment() {

    //define the binding variable for this fragment.
    private lateinit var binding: FragmentLoginScreenBinding

    //define loginViewmodel
    private val viewmodel:LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //assign the binding variable
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        //to hide the bottom navigation in the main activity
//        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.visibility = View.GONE
//
//        //to hide the actionbar.
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()



        //to get the instance of the firebase auth.
        val firebaseAuth = FirebaseAuth.getInstance()

        //to handle the click on the login button.
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    viewmodel.getUsername()
                    Toast.makeText(activity, "Logged in successfully!", Toast.LENGTH_SHORT).show()
                    SharedPrefUtil.get().setUserID(firebaseAuth.currentUser?.uid)
                    Log.d(TAG,"USERID is: ${firebaseAuth.currentUser?.uid}")
                    findNavController().navigate(R.id.action_loginScreen_to_timelineScreen)
                }.addOnFailureListener {
                    Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    activity,
                    "Email or Password is empty! please fill all fields.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.doNotHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_signUpScreen)
        }


    }
}