package com.example.gadshealthteam8


import android.R
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.gadshealthteam8.databinding.ActivityAdminMainBinding
import com.google.firebase.firestore.DocumentReference
import java.text.SimpleDateFormat
import java.util.*

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminMainBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var category: String
    private lateinit var documentId : String
    var documentReference: DocumentReference? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)


        binding.tipButton.setOnClickListener {
            val tip =  binding.tipInput.text.toString().trim()
            val author = binding.tipInputAuthor.text.toString().trim()
            val full = binding.fullInfo.text.toString().trim()

            if (tip.isNotEmpty() && author.isNotEmpty() && full.isNotEmpty()) {
                addQuote(tip , author , full)
            }
            else{
                Toast.makeText(this , "Credentials not complete" , Toast.LENGTH_SHORT).show()
            }

        }
        val items = arrayOf(
            "Select Category",
            "LifeStyles",
            "Nutrition",
            "Diseases",
            "Mental",
            "Random"
        )

        binding.addCategory.adapter =
            ArrayAdapter(this , R.layout.simple_spinner_dropdown_item , items)

        binding.addCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                category = binding.addCategory.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }




    }


    private fun addQuote(tip: String, author: String, full: String) {
        progressDialog.show()
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val date = formatter.format(now)

        val db =FirebaseUtils().fireStoreDatabase.collection("HealthTips").document("Users").collection(category)
        // val jobDocId: String = documentReference.getId()
        val docId : String = db.id
        val hashMap = hashMapOf<String , Any>(
            "HealthTips" to tip ,
            "HealthTipsUploadDate" to date ,
            "HealthTipsAuthor" to author ,
            "HealthTipsDocumentId" to docId,
            "HealthTipsFullInformation" to full
        )

        db.add(hashMap)
            .addOnSuccessListener {
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Quote Uploaded Successfully", Toast.LENGTH_SHORT).show()
                Log.d("admin", "Added document with ID ${it.id}")


            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Error adding document $exception", Toast.LENGTH_SHORT).show()
                Log.w("admin", "Error adding document $exception")
            }



    }
}