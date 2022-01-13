package com.example.thebird.adapterimport

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thebird.R
import com.example.thebird.model.Post
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class PostsAdapter() :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.timestamp == newItem.timestamp
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
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Post>) {
        differ.submitList(list)
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
    }
}