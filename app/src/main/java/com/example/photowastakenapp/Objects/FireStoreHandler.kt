package com.example.photowastakenapp.Objects

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreHandler {

    private val db = Firebase.firestore

    fun uploadUriPhotoToFireStore(imageUri: String) {
        // Add a new document with a generated ID
        val uri = hashMapOf(
            "Uri" to imageUri,
        )

        // Add a new document with a generated ID
        db.collection("UriImages")
            .add(uri)
            .addOnSuccessListener { documentReference -> }
            .addOnFailureListener { e -> }
    }


}