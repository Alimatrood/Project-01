package com.example.thebird

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thebird.util.SharedPrefUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashScreen : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedPrefUtil.init(requireActivity())

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.GONE

        object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                //if the user logged in , take him to the Home Screen, if not , to Welcomeing Screen.
                val username = SharedPrefUtil.get().getUserId()
                if (username != "") {
                    findNavController().navigate(R.id.action_splashScreen_to_timelineScreen)
                } else {
                    findNavController().navigate(R.id.action_splashScreen_to_loginScreen)
                }
            }

        }.start()
    }
}