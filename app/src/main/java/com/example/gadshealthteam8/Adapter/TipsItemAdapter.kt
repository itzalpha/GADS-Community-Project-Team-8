package com.example.gadshealthteam8.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gadshealthteam8.R
import com.example.gadshealthteam8.checkLike
import com.example.gadshealthteam8.model.TipsModel
import com.example.gadshealthteam8.ui.lifestyles.QuoteInfoActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_tips_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class TipsItemAdapter(val context: Context, private var userList : ArrayList<TipsModel>) :
    RecyclerView.Adapter<TipsItemAdapter.ViewHolder>() {

    private lateinit var personName  : String
    private lateinit var personEmail : String
    private lateinit var personPhoto : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_tips_list ,parent , false  )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : TipsModel = userList[position]

        // TODO:Check SignUp Google Information
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        if (acct != null){
            personName = acct.displayName.toString()
            personEmail = acct.email.toString()
            personPhoto = acct.photoUrl.toString()
        }

        // TODO:Public Information
        val id = FirebaseAuth.getInstance().currentUser
        val currentUserId = id!!.uid
        val myUserId = user.HealthTipsDocumentId
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val date = formatter.format(now)
        val db = FirebaseFirestore.getInstance()

        // TODO:Assign Value to xml object
        holder.quote.text = user.HealthTips
        holder.quote_author.text ="~"+user.HealthTipsAuthor+"~"

        // TODO:Perform Action in xml object

        holder.quote_image_like.setOnClickListener {
            val dob = db.collection("HealthTips").document("Admin").collection("Likes").document(myUserId.toString()).collection("Users").document(currentUserId)

            dob.get().addOnCompleteListener {  task ->

                if (task.result.exists()){
                    db.collection("HealthTips").document("Admin").collection("Likes").document(myUserId.toString()).collection("Users").document(currentUserId).delete()
                    Toast.makeText(context , "Unlike " , Toast.LENGTH_LONG).show()
                }else
                {

                    val doc = db.collection("HealthTips").document("Admin").collection("Likes").document(myUserId.toString()).collection("Users").document(currentUserId)

                    val hashMap = hashMapOf<String , Any>(
                        "UserLikedId" to currentUserId ,
                        "UserLikedDate" to date ,
                        )
                    doc.set(hashMap)
                        //  doc.add(hashMap)
                        .addOnSuccessListener {
                            Toast.makeText(context , "Like" , Toast.LENGTH_LONG).show()
                        }
                }
            }


        }

        val dob = db.collection("HealthTips").document("Admin").collection("Likes").document(myUserId.toString()).collection("Users").document(currentUserId)
        checkLike(dob ,holder.quote_image_like  )

        holder.quote_image_share.setOnClickListener {
            ShareCompat.IntentBuilder.from(context as Activity)
                .setType("text/plain")
                .setChooserTitle(R.string.app_name)
                .setText(user.HealthTips + "\n" + "~"+user.HealthTipsAuthor+"~" + "\n")
                .startChooser()

        }

        db.collection("HealthTips").document("Admin").collection("Likes").document(myUserId.toString()).collection("Users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var count = 0
                    for (document in task.result) {
                        count++
                    }
                    Log.d("TAG", count.toString() + "")
                    holder.item_quote_text_like_numbers.text = (count.toString() + "")
                } else {
                    Log.d("Tag", "Error getting documents: ", task.exception)
                }
            }

        db.collection("HealthTips").document("Admin").collection("Likes").document(myUserId.toString()).collection("Users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var count = 0
                    for (document in task.result) {
                        count++
                    }
                    Log.d("TAG", count.toString() + "")
                    holder.quote_number_like.text = (count.toString() + "")
                } else {
                    Log.d("Tag", "Error getting documents: ", task.exception)
                }
            }

        holder.layout.setOnClickListener {
            val intent = Intent(context, QuoteInfoActivity::class.java)
            intent.putExtra("Quote", user.HealthTips)
            intent.putExtra("QuoteAuthor", user.HealthTipsAuthor)
            intent.putExtra("QuoteUploadDate", user.HealthTipsUploadDate)
            intent.putExtra("category" , user.HealthTipsCategory)
            intent.putExtra("docId" ,  user.HealthTipsDocumentId)
              //user information
            intent.putExtra("UserPersonName" , personName )
            intent.putExtra("UserPersonEmail" , personEmail)
            intent.putExtra("UserPersonPhoto" , personPhoto)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun Filteredlist(filterlist: ArrayList<TipsModel>) {
        userList = filterlist
        notifyDataSetChanged()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val quote = view.item_quote_text
        val quote_author = view.item_quote_text_author
        val quote_image_like = view.quote_image_like
        var quote_number_like = view.item_quote_text_like_numbers
        val quote_image_share = view.quote_image_share
        val layout = view.quote_item_layout
        val item_quote_text_like_numbers= view.item_quote_text_like_numbers
    }

}