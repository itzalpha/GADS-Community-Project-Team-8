package com.example.gadshealthteam8

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.gadshealthteam8.databinding.ActivitySplashMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.BuildConfig
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SplashMainActivity : AppCompatActivity() {

    private val TAG = SplashMainActivity::class.qualifiedName
    private lateinit var binding : ActivitySplashMainBinding

      private val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()


       val job =  scope.launch {
                delay(3000)
        }


        binding.retryImage.setOnClickListener {
            googleSign()
        }

        job.invokeOnCompletion {
            if (auth.currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }else{
                googleSign()
            }

        }



    }

    private fun googleSign() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(!BuildConfig.DEBUG , true)
                .build(),
            RC_SIGN_IN
        )

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this , "Sign-up Successful" , Toast.LENGTH_SHORT).show()
                val acct = GoogleSignIn.getLastSignedInAccount(this)
                if (acct != null){
                    val personName = acct.displayName
                    val personFamily = acct.familyName
                    val personGivenName = acct.givenName
                    val personEmail = acct.email
                    val personId = acct.id
                    val personPhoto : Uri? = acct.photoUrl

                    UserAddedToDatabase(personName , personFamily , personGivenName , personEmail , personId , personPhoto)

                }


                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(USER_ID, user!!.uid)
                startActivity(intent)
            } else {
                Toast.makeText(this , "Sign-in failed" , Toast.LENGTH_SHORT).show()
                binding.splashImage.visibility = View.GONE
                binding.retryImage.visibility = View.VISIBLE
                binding.failedRetry.visibility = View.VISIBLE
                binding.failedRetry.text = "Sign in Failed "+ "\nplease try again \n" + response!!.error
                //Log.e(TAG, "Sign-in failed", response!!.error)
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun UserAddedToDatabase(personName: String?, personFamily: String?, personGivenName: String?, personEmail: String?, personId: String?, personPhoto: Uri?) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val date = formatter.format(now)

        val db = FirebaseUtils().fireStoreDatabase.collection("HealthTips").document("Google Authenticated Users").collection("Health Tips Users").document(
            personId.toString()
        )
        val docId : String = db.id
        val hashMap = hashMapOf<String , Any>(
            "PersonName" to personName.toString() ,
            "PersonFamily" to personFamily.toString() ,
            "PersonGivenName" to personGivenName.toString() ,
            "PersonDocumentId" to docId ,
            "PersonEmail" to personEmail.toString()  ,
            "PersonId" to personId.toString() ,
            "PersonPhoto" to personPhoto.toString() ,
            "PersonSignInDate" to  date

        )

        db.set(hashMap)
            .addOnSuccessListener {
                Log.d("MainActivity", "Success")

            }
            .addOnFailureListener {
                Log.w("MainActivity", "Failed")
            }



    }

    companion object {
        const val USER_ID = "user_id"
        const val RC_SIGN_IN = 15
    }


}