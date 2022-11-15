package com.example.gadshealthteam8.support

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gadshealthteam8.FirebaseUtils
import com.example.gadshealthteam8.databinding.FragmentFeedbackBinding

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class FeedbackFragment : Fragment() {

    private  var _binding: FragmentFeedbackBinding? = null
    private lateinit var progressDialog : ProgressDialog

    private lateinit var personName : String
    private lateinit var personEmail : String
    private lateinit var currentUserId :  String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        val id = FirebaseAuth.getInstance().currentUser
        currentUserId = id!!.uid


        val acct = GoogleSignIn.getLastSignedInAccount(context)
        if (acct != null){
            personName = acct.displayName.toString()
            personEmail = acct.email.toString()

        }

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)

        _binding!!.sendFeedBackButton.setOnClickListener {
            var feedback  = _binding!!.feedBackReport.text.toString().trim()
            if (feedback!!.isNotEmpty()){
                   saveFeedBack(feedback)
            }else{
                Toast.makeText(context , "input feedback" , Toast.LENGTH_LONG).show()
            }
        }


        return _binding!!.root
    }

    private fun saveFeedBack(feedback: String) {

        progressDialog.show()
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val date = formatter.format(now)
        val hashMap = HashMap<String , Any>()
        hashMap["FeedBack"] = feedback
        hashMap["FeedBackDate"] = date
        hashMap["FeedBackUserId"] = currentUserId
        hashMap["FeedBackUsername"] = personName
        hashMap["FeedBackUserEmail"] = personEmail

        FirebaseUtils().fireStoreDatabase.collection("MotivationVerse").document("Admin").collection("MotivationVerseFeedBack")
            .add(hashMap)
            .addOnSuccessListener {
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(context, "Feedback sent Successfully", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { exception ->
                if (progressDialog.isShowing)progressDialog.dismiss()
                Toast.makeText(context, "Error sending feedback $exception", Toast.LENGTH_SHORT).show()

            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}