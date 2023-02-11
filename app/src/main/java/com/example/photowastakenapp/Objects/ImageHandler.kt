package com.example.photowastakenapp.Objects

import com.example.photowastakenapp.Services.ImageService
import com.example.photowastakenapp.Utils.ImageUtils
import com.example.photowastakenapp.Utils.SharedPrefUtils


class ImageHandler {

    private val storageFBHandler = StorageFBHandler()

    fun handlerNewPhotos() {
        // get the old image date
        val oldImageDate: Long? =
            SharedPrefUtils.getInstance().getLong(ImageService.IMAGE_DATE_KEY, 0)
        // get the last image from storage
        val newImage = ImageUtils.getInstance()?.getTheLastImage()
        // get the date of new image
        val newImageDate = ImageUtils.getInstance()?.getImageDateTaken(newImage)
        // check if the dates are not equal (means there is new photo)
        val isNewImage = checkAndUpdateForNewImage(oldImageDate, newImageDate)

        if (isNewImage) {
            // upload the new photo to DB
            storageFBHandler.uploadImageToStorage(newImage)

        }

    }

    // upload the new photo to DB
    private fun checkAndUpdateForNewImage(oldImageDate: Long?, newImageDate: Long?): Boolean {

        newImageDate?.let { newDate ->
            oldImageDate?.let { oldDate ->
                if (newDate > oldDate) {
                    SharedPrefUtils.getInstance().putFLong(ImageService.IMAGE_DATE_KEY, newDate)
                    return true
                }
            }

        }
        return false
    }


}