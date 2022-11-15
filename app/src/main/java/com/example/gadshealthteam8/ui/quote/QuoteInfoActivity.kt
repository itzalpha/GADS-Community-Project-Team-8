package com.example.gadshealthteam8.ui.quote

import android.app.Activity
import android.app.ProgressDialog
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gadshealthteam8.FirebaseUtils
import com.example.gadshealthteam8.Review.ReviewAdapter.ReviewItemAdapter
import com.example.gadshealthteam8.Review.ReviewModel.ReviewModel
import com.example.gadshealthteam8.checkLike
import com.example.gadshealthteam8.databinding.ActivityQuoteInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener

import kotlinx.android.synthetic.main.activity_quote_info.*
import kotlinx.android.synthetic.main.activity_quote_info.comments_review_layout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class QuoteInfoActivity : AppCompatActivity() {

    private lateinit var userArrayList: ArrayList<ReviewModel>
    private lateinit var myAdapter : ReviewItemAdapter
    private var db = FirebaseFirestore.getInstance()
    private lateinit var progressDialog : ProgressDialog
    private lateinit var UserPersonName : String
    private lateinit var UserPersonEmail : String
    private lateinit var UserPersonPhoto : String
    private lateinit var category : String
    private lateinit var docId : String

    private lateinit var binding : ActivityQuoteInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val id = FirebaseAuth.getInstance().currentUser
        val currentUserId = id!!.uid
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val date = formatter.format(now)

        val Quote = intent.getStringExtra("Quote")
        val QuoteAuthor = intent.getStringExtra("QuoteAuthor")
        val QuoteUploadDate = intent.getStringExtra("QuoteUploadDate")
         category = intent.getStringExtra("category").toString()
         docId = intent.getStringExtra("docId").toString()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)

        UserPersonName = intent.getStringExtra("UserPersonName").toString()
        UserPersonEmail = intent.getStringExtra("UserPersonEmail").toString()
        UserPersonPhoto = intent.getStringExtra("UserPersonPhoto").toString()


        supportActionBar?.hide()
        info_quote_author.text = "~~ $QuoteAuthor ~~"
        info_quote_information.text = Quote


        quote_info_image_comment.setOnClickListener {
            if (comments_review_layout.visibility == View.GONE){
                comments_review_layout.visibility = View.VISIBLE
            }else{
                comments_review_layout.visibility = View.GONE
            }


        }

        quote_info_image_share.setOnClickListener {
            ShareCompat.IntentBuilder.from(this@QuoteInfoActivity)
                .setType("text/plain")
                .setChooserTitle("R.string.app_name")
                .setText("$Quote\n~$QuoteAuthor~\n")
                .startChooser()
        }

        binding.quoteInfoImageLike.setOnClickListener {
        /////    val dob = db.collection("MotivationalVerse").document("Admin").collection("Book_Likes").document(docId.toString()).collection("Users").document(currentUserId)
            val dob = db.collection("MotivationVerse").document("Admin").collection("Quote_Likes").document(docId.toString()).collection("Users").document(currentUserId)


            dob.get().addOnCompleteListener {  task ->

                if (task.result.exists()){
                    Toast.makeText(this , "UnLike " , Toast.LENGTH_LONG).show()
                }else
                {

                ///    val doc = db.collection("MotivationalVerse").document("Admin").collection("Book_Likes").document(docId.toString()).collection("Users").document(currentUserId)
                    val doc = db.collection("MotivationVerse").document("Admin").collection("Quote_Likes").document(docId.toString()).collection("Users").document(currentUserId)

                    val hashMap = hashMapOf<String , Any>(
                        "UserLikedId" to currentUserId ,
                        "UserLikedDate" to date ,
                    )
                    doc.set(hashMap)
                        //  doc.add(hashMap)
                        .addOnSuccessListener {
                            Toast.makeText(this , "Like" , Toast.LENGTH_LONG).show()
                        }

                }

            }
        }

     ///   db.collection("MotivationalVerse").document("Admin").collection("Book_Likes").document(docId.toString()).collection("Users").get()
        db.collection("MotivationVerse").document("Admin").collection("Quote_Likes").document(docId.toString()).collection("Users").get()

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var count = 0
                    for (document in task.result) {
                        count++
                    }
                    Log.d("TAG", count.toString() + "")
                    binding.itemQuoteInfoTextLikeNumbers.text= (count.toString() + "")
                } else {
                    Log.d("Tag", "Error getting documents: ", task.exception)
                }
            }


       //// val dob = db.collection("MotivationalVerse").document("Admin").collection("Book_Likes").document(docId.toString()).collection("Users").document(currentUserId)
        val dob = db.collection("MotivationVerse").document("Admin").collection("Quote_Likes").document(docId.toString()).collection("Users").document(currentUserId)

        checkLike(dob , binding.imageLike )









        quote_review_button.setOnClickListener {
            val review =  quote_review_text.text.toString().trim()
            if (review.isNotEmpty()){
                addReview()
            }else{
                Toast.makeText(this , "Review is empty" , Toast.LENGTH_SHORT).show()
            }
        }


        val recyclerView : RecyclerView = quote_review_recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        myAdapter = ReviewItemAdapter(this , userArrayList)
        recyclerView.adapter = myAdapter
        EventChangeListener()


        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("MotivationVerse").document("Admin").collection("Reviews").document("QuoteReviews").collection(docId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var count = 0
                    for (document in task.result) {
                        count++
                    }
                    Log.d("TAG", count.toString() + "")
                    quote_recycler_count.text = (count.toString() + "")
                } else {
                    Log.d("Tag", "Error getting documents: ", task.exception)
                }
            }


    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("MotivationVerse").document("Admin").collection("Reviews").document("QuoteReviews").collection(docId)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null){
                        return
                    }

                    for (dc : DocumentChange in  value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            userArrayList.add(dc.document.toObject(ReviewModel::class.java))

                        }

                    }
                    myAdapter.notifyDataSetChanged()


                }

            })
    }

    private fun addReview() {
        progressDialog.show()
        val review =  quote_review_text.text.toString().trim()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val date = formatter.format(now)

        val db = FirebaseUtils().fireStoreDatabase.collection("MotivationVerse").document("Admin").collection("Reviews").document("QuoteReviews").collection(docId)
        val docId : String = db.id
        val hashMap = hashMapOf<String , Any>(
            "UserReviewName" to UserPersonName ,
            "UserReviewComment" to review ,
            "UserReviewDate" to date ,
            "UserReviewGmail" to UserPersonEmail ,
            "UserReviewImage" to UserPersonPhoto ,
            "UserReviewDocumentId" to docId

        )

        db.add(hashMap)
            .addOnSuccessListener {
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Review Added Successfully", Toast.LENGTH_SHORT).show()



            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Error adding Review $exception", Toast.LENGTH_SHORT).show()

            }

    }


}