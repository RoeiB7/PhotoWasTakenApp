package com.example.photowastakenapp.Objects

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class StorageFBHandler {

    private val storage = Firebase.storage
    private val storageRef = storage.reference


    fun uploadImageToStorage(newImage: Uri?) {
        val imagesRef: StorageReference =
            storageRef.child("images/" + System.currentTimeMillis())


        val uploadTask = newImage?.let { imagesRef.putFile(it) }

        uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imagesRef.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val fireStoreHandler =  FireStoreHandler()
                fireStoreHandler.uploadUriPhotoToFireStore(downloadUri.toString())
            } else {

            }
        }


    }
}