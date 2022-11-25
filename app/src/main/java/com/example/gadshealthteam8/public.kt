package com.example.gadshealthteam8

import android.graphics.Color
import android.widget.ImageView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class public{

}


fun  checkLike(dob: DocumentReference, ImageLike: ImageView) {


    dob.get().addOnSuccessListener { task ->
        if (task.exists()){
            ImageLike.setColorFilter(Color.RED)
        }
        else{

        }
     }


}


class FirebaseUtils {
    val fireStoreDatabase = FirebaseFirestore.getInstance()
}