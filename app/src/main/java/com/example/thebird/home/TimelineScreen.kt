package com.example.thebird.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.thebird.R
import com.example.thebird.databinding.FragmentTimelineScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TimelineScreen : Fragment() {

    //define a binding variable
    private lateinit var binding:FragmentTimelineScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize binding variable
        binding = FragmentTimelineScreenBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //to hide the back arrow in the action bar inside the home fragment.
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //to hide the actionbar.
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        //to show the bottom navigation.
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE

        //open the newPost screen when the floatingActionButton is pressed
        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(R.id.action_timelineScreen_to_newPostScreen)
        }

    }
}