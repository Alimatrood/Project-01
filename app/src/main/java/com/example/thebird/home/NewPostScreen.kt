package com.example.thebird.home

import android.os.Bundle
import android.os.SharedMemory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.thebird.R
import com.example.thebird.databinding.FragmentNewPostScreenBinding
import com.example.thebird.model.Post
import com.example.thebird.util.SharedPrefUtil
import java.util.*

class NewPostScreen : Fragment() {

    // define a variable for binding for the view with the fragment
    private lateinit var binding: FragmentNewPostScreenBinding

    //define the viewmodel
    private val viewModel: NewPostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //initialize the binding variable
        binding = FragmentNewPostScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        binding.postButton.setOnClickListener {
            val text = binding.postEditText.text.toString()
            val username = SharedPrefUtil.get().getUsername()
            val timestamp: Long = Calendar.getInstance().timeInMillis
            viewModel.addPost(Post(text, username!!, timestamp))
        }

    }

    private fun observe() {
        viewModel.successfullyAddingPost.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                viewModel.successfullyAddingPost.postValue(null)
                findNavController().navigate(R.id.action_newPostScreen_to_timelineScreen)

            }
        })
        viewModel.errorAddingPost.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                viewModel.errorAddingPost.postValue(null)
            }

        })

    }

}