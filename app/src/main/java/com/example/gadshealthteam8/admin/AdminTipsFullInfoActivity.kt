package com.example.gadshealthteam8.admin

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gadshealthteam8.FirebaseUtils
import com.example.gadshealthteam8.databinding.ActivityAdminTipsFullInfoBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AdminTipsFullInfoActivity : AppCompatActivity() {

    private lateinit var docId : String
    private lateinit var pd : ProgressDialog
     private lateinit var db: FirebaseFirestore
     private lateinit var category : String
    private lateinit var binding : ActivityAdminTipsFullInfoBinding
    var documentReference: DocumentReference? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTipsFullInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        docId = intent.getStringExtra("docId").toString()
        category = intent.getStringExtra("HealthTipsCategory").toString()


        db = FirebaseFirestore.getInstance()
        pd = ProgressDialog(this)
        pd.setCanceledOnTouchOutside(false)
        pd.setMessage("loading")
        pd.show()


        val reference: DocumentReference
        val firestore = FirebaseFirestore.getInstance()
        reference =
            firestore.collection("HealthTips").document("Users").collection(category)
                .document(docId)
      reference.get()
            .addOnCompleteListener { task ->
                if (task.result.exists()) {
                    pd.dismiss()
                    val tipInput = task.result.getString("HealthTips")
                    val fullInfo = task.result.getString("HealthTipsFullInformation")

                    binding.tipInput.setText(tipInput)
                    binding.fullInfo.setText(fullInfo)

                } else {
                    pd.dismiss()
                    Toast.makeText(
                        this,
                        "Cant Find Tips Information",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        binding.updateHealthTipButton.setOnClickListener {
            val tip =  binding.tipInput.text.toString().trim()
            val full = binding.fullInfo.text.toString().trim()

            if (tip.isNotEmpty()  && full.isNotEmpty()) {
                addTip(tip  , full)
            }
            else{
                Toast.makeText(this , "Credentials not complete" , Toast.LENGTH_SHORT).show()
            }


        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addTip(tip: String, full: String) {
        pd.show()
        val save = FirebaseUtils().fireStoreDatabase.collection("HealthTips").document("Users").collection(category).document(docId)
        val hashMap = hashMapOf<String , Any>(
            "HealthTips" to tip ,
            "HealthTipsFullInformation" to full
        )

        save!!.update(hashMap)
            .addOnSuccessListener {
                if (pd.isShowing)pd.dismiss()
                Toast.makeText(this, "Tips Updated Successfully", Toast.LENGTH_SHORT).show()



            }
            .addOnFailureListener { exception ->
                if (pd.isShowing)pd.dismiss()
                Toast.makeText(this, "Error adding document $exception", Toast.LENGTH_SHORT).show()

            }


    }
}