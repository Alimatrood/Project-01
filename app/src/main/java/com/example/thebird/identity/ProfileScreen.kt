package com.example.thebird.identity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import com.example.thebird.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.thebird.model.User
import com.example.thebird.util.SharedPrefUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.OnProgressListener
import com.squareup.picasso.Picasso


class ProfileScreen : Fragment() {
lateinit var profileImage:de.hdodenhof.circleimageview.CircleImageView
lateinit var fname:String
lateinit var username:String
lateinit var email:String
var user= FirebaseAuth.getInstance().currentUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        var v=  inflater.inflate(R.layout.fragment_profile_screen, container, false)

        //to hide the back arrow in the action bar inside the home fragment.
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        profileImage=v.findViewById(R.id.profile_image)
        var nameTextView=v.findViewById<TextView>(R.id.textViewProfileName)
        var userNameTextView=v.findViewById<TextView>(R.id.textViewUserName)


//        db.collection("users").document(uid)
//            .get()
//            .addOnSuccessListener { result ->
//                fname= result.getString("firstname").toString()
//                username= result.getString("username").toString()
//                email=result.getString("email").toString()
//                nameTextView.text=fname
//                userNameTextView.text=username

        ProfileViewModel().getUserData().observe(viewLifecycleOwner,{
            nameTextView.text=it.firstname
            userNameTextView.text=it.username
            if (it.avatar.isNotBlank()){
                Picasso.get().load(it.avatar).into(profileImage)
            }





        })


//                ProfileViewModel().mLiveData.observe(viewLifecycleOwner,{
//                    if(it)
//                    {
//                        Picasso.get().load(result.getString("avatar") as Uri).into(profileImage)
//                    }
//
//                })



        profileImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(50)            //Final image size will be less than 1 MB(Optional)
                .start()
        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            profileImage.setImageURI(uri)
            ProfileViewModel().updateAvatar(uri.toString()).observe(this,{

            })


        }
     else if (resultCode == ImagePicker.RESULT_ERROR) {
        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
    }


}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                Firebase.auth.signOut()
                Toast.makeText(context, "Logout successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_profileScreen_to_loginScreen)
                SharedPrefUtil.get().setUserID(null)
                SharedPrefUtil.get().setUsername(null)

            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun uploadImage(uri: Uri) {


        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        val refrence :StorageReference = FirebaseStorage.getInstance().getReference(userUid)
         refrence.child("images")
        //val uploadTask = refrence.child("images")
        refrence.putFile(uri)

                .addOnSuccessListener { // Image uploaded successfully

                    Toast
                        .makeText(
                            context,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                .addOnFailureListener { e -> // Error, Image not uploaded
                    Toast
                        .makeText(
                            context,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                .addOnProgressListener(
                    object : OnProgressListener<UploadTask.TaskSnapshot?> {
                        // Progress Listener for loading
                        // percentage on the dialog box
                        override fun onProgress(
                            taskSnapshot: UploadTask.TaskSnapshot
                        ) {
                            val progress = ((100.0
                                    * taskSnapshot.bytesTransferred
                                    / taskSnapshot.totalByteCount))


                        }
                    })
        }

    }



