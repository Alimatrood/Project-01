package com.example.thebird.identity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toDrawable
import com.example.thebird.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileScreen : Fragment() {
lateinit var profileImage:de.hdodenhof.circleimageview.CircleImageView
var fname:String=""
 var username:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v=  inflater.inflate(R.layout.fragment_profile_screen, container, false)
        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        profileImage=v.findViewById(R.id.profile_image)
        var nameTextView=v.findViewById<TextView>(R.id.textViewProfileName)
        var userNameTextView=v.findViewById<TextView>(R.id.textViewUserName)

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { result ->
                 fname= result.getString("firstname").toString()
                 username= result.getString("username").toString()
            }

        nameTextView.text=fname
        userNameTextView.text=username





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

        }
     else if (resultCode == ImagePicker.RESULT_ERROR) {
        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
    }
}

}