package com.example.gadshealthteam8

import android.graphics.Color
import android.widget.ImageView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class public{

}


fun  checkLike(dob: DocumentReference, ImageLike: ImageView) {

    // dob.get().addOnCompleteListener { task ->

    dob.get().addOnSuccessListener { task ->

   //      if (task.result.exists()) {

        if (task.exists()){
            ImageLike.setColorFilter(Color.RED)
        }
        else{

        }


   //      }
   // else {
           //  ImageLike.setColorFilter(Color.BLUE)
      //   }

     }


}


class FirebaseUtils {
    val fireStoreDatabase = FirebaseFirestore.getInstance()
}