package com.example.photowastakenapp.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.File

class ImageUtils private constructor(private val context: Context) {

    private val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val projection = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.Media._ID,
        MediaStore.Images.ImageColumns.DATE_ADDED,
        MediaStore.Images.ImageColumns.MIME_TYPE
    )


    companion object {
        // application Context
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: ImageUtils? = null

        fun init(context: Context) {
            INSTANCE = INSTANCE ?: ImageUtils(context)
        }

        fun getInstance(): ImageUtils? {
            return INSTANCE

        }


    }

    /**
     * get the amount of picture inside gallery
     */
    fun getAmountImage(): Int {
        val cursor: Cursor = context.contentResolver.query(
            uriExternal, projection, null,
            null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
        )!!
        val amountImage = cursor.count
        cursor.close()
        return amountImage
    }

    /**
     * get the last image in the gallery
     */
    fun getTheLastImage(): Uri? {
        var imageUri: Uri? = null
        val cursor: Cursor = context.contentResolver.query(
            uriExternal, projection, null,
            null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
        )!!

        if (cursor.moveToFirst()) {
            val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val imageId: Long = cursor.getLong(columnIndexID)
            imageUri = Uri.withAppendedPath(uriExternal, "" + imageId)

        }

        getImageDateTaken(imageUri)


        cursor.close()
        return imageUri
    }


    fun getImageDateTaken(imageUri: Uri?): Long? {
        val path = getPath(imageUri)
        val file = path?.let { File(it) }

        file?.let {
            if (file.exists()) {
                return file.lastModified()
            }

        }
        return null
    }

    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            uri?.let { context.contentResolver.query(it, projection, null, null, null) }
                ?: return null
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(columnIndex)
        cursor.close()
        return s
    }

}

