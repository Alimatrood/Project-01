package com.example.thebird.identity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.thebird.R
import com.example.thebird.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : Fragment() {

    //define the binding variable for this fragment.
    private lateinit var binding: FragmentLoginScreenBinding

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

        val firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(activity, "Logged in successfully!", Toast.LENGTH_SHORT).show()
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