package com.example.gadshealthteam8.admin


import android.R
import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.gadshealthteam8.FirebaseUtils
import com.example.gadshealthteam8.databinding.ActivityAdminMainBinding
import com.google.firebase.firestore.DocumentReference
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AdminAddTipsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminMainBinding
    private lateinit var progressDialog : ProgressDialog
    private lateinit var category: String
    private lateinit var documentId : String
    var documentReference: DocumentReference? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)


        binding.tipButton.setOnClickListener {
            val tip =  binding.tipInput.text.toString().trim()
            val full = binding.fullInfo.text.toString().trim()

            if (tip.isNotEmpty()  && full.isNotEmpty()) {
                addTip(tip  , full)
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTip(tip: String, full: String) {
        progressDialog.show()
        documentReference = FirebaseUtils().fireStoreDatabase.collection("HealthTips").document("Users").collection(category).document()
        val docId = documentReference!!.id
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val date = current.format(formatter)

        val hashMap = hashMapOf<String , Any>(
            "HealthTips" to tip ,
            "HealthTipsCategory" to category ,
            "HealthTipsUploadDate" to date ,
            "HealthTipsDocumentId" to docId,
            "HealthTipsFullInformation" to full
        )

        documentReference!!.set(hashMap)
            .addOnSuccessListener {
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Tips Uploaded Successfully", Toast.LENGTH_SHORT).show()



            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(this, "Error adding document $exception", Toast.LENGTH_SHORT).show()

            }



    }
}