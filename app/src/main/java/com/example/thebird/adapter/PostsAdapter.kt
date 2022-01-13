package com.example.thebird.adapterimport

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thebird.R
import com.example.thebird.home.TimelineViewModel
import com.example.thebird.model.Post
import org.w3c.dom.Text
import java.text.SimpleDateFormat

private const val TAG = "PostsAdapter"
class PostsAdapter(private val viewmodel: TimelineViewModel) :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    private val favoriteList = mutableListOf<Post>()
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostsViewHolder {
        return PostsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.post_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.postTextView.text = item.postText
        holder.usernameTextView.text = item.username
        holder.timestampTextView.text = SimpleDateFormat("yyyy-MM-dd hh:mm").format(item.timestamp)
        holder.avatarImageView.visibility = View.GONE
        holder.imageView.visibility = View.GONE
        Log.d(TAG,"$favoriteList")
        if (favoriteList.contains(item)) {
            holder.favoriteButton.isChecked = true
        }
        holder.favoriteButton.setOnClickListener {
            if (holder.favoriteButton.isChecked)
                viewmodel.addToFavorites(item)
            else
                viewmodel.removeFromFavorites(item)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Post>, favorites: List<Post>) {
        differ.submitList(list)
        favoriteList.addAll(favorites)
    }

    fun getList(): List<Post> {
        return differ.currentList
    }


    class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTextView: TextView = itemView.findViewById(R.id.post_text)
        val imageView: ImageView = itemView.findViewById(R.id.post_image)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text)
        val usernameTextView: TextView = itemView.findViewById(R.id.username_textview)
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatar_image_view)
        val favoriteButton: ToggleButton = itemView.findViewById(R.id.favorite_toggle_button)
    }
}