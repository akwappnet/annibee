package com.devstree.annibee.model

import android.content.Context
import android.provider.MediaStore

class ImageGallery {

    fun listOfImages(context: Context): ArrayList<String> {
        val listOfAllImages = ArrayList<String>()
        var absolutePath: String

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projections = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Video.Media.DATE_TAKEN

        val cursor = context.contentResolver.query(uri, projections, null, null, orderBy+" DESC")

        val columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                absolutePath = columnIndexData?.let { cursor.getString(it) }.toString()
                listOfAllImages.add(absolutePath)
            }
        }

        return listOfAllImages
    }
}