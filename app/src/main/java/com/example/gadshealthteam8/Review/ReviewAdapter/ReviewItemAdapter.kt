package com.example.gadshealthteam8.Review.ReviewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gadshealthteam8.R
import com.example.gadshealthteam8.Review.ReviewModel.ReviewModel

import kotlinx.android.synthetic.main.item_comment.view.*

class ReviewItemAdapter(val context : Context , private val userList : ArrayList<ReviewModel>) :
     RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment ,parent , false  )
        return ViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : ReviewModel = userList[position]
      holder.review_comments.text = user.UserReviewComment
        holder.review_user_name.text = user.UserReviewName

        Glide.with(context).load(user.UserReviewImage).into( holder.review_image)

    }

    override fun getItemCount(): Int {
        return userList.size
        notifyDataSetChanged()
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val review_image = view.profileImage_comments
    val review_user_name = view.usernameComment
    val review_comments = view.comments

}
