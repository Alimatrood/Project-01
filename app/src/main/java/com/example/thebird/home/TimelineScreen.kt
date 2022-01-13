package com.example.thebird.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.thebird.R
import com.example.thebird.adapterimport.PostsAdapter
import com.example.thebird.databinding.FragmentTimelineScreenBinding
import com.example.thebird.model.Post
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "TimelineScreen"
class TimelineScreen : Fragment() {

    //define a binding variable
    private lateinit var binding: FragmentTimelineScreenBinding

    //define the viewmodel
    private val viewmodel: TimelineViewModel by activityViewModels()

    //define the adapter for posts
    private lateinit var adapter: PostsAdapter

    private val allPostsList = mutableListOf<Post>()
    private val favoriteList = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize binding variable
        binding = FragmentTimelineScreenBinding.inflate(inflater, container, false)

        //setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        viewmodel.getPosts()
       // viewmodel.getAllFavoritesForUser()


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
        adapter = PostsAdapter(viewmodel)
        binding.recyclerView.adapter = adapter


    }

    private fun observe() {
        viewmodel.postsListMutableLiveData.observe(viewLifecycleOwner, {
            it?.let {
                allPostsList.clear()
                allPostsList.addAll(it)
               // adapter.submitList(it)
                Log.d(TAG,"ALL POSTS LIST inside its observe: $allPostsList")
                //viewmodel.postsListMutableLiveData.postValue(null)
                viewmodel.getAllFavoritesForUser()

            }
        })
        viewmodel.errorPostsList.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                viewmodel.errorPostsList.postValue(null)
            }
        })
        viewmodel.favoriteListMutableLiveData.observe(viewLifecycleOwner,{
            it?.let {
                favoriteList.clear()
                favoriteList.addAll(it)
                Log.d(TAG,"Favorite list inside its observe: $favoriteList")
                adapter.submitList(allPostsList,favoriteList)

            }
        })
        viewmodel.errorFavoritesList.observe(viewLifecycleOwner,{
            Toast.makeText(requireActivity(), "this is the error", Toast.LENGTH_SHORT).show()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.direct_messages_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.direct_messages -> {
                findNavController().navigate(R.id.action_timelineScreen_to_directMessagesScreen)
            }

        }
        return super.onOptionsItemSelected(item)
    }


}