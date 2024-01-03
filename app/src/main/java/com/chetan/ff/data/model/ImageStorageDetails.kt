package com.chetan.ff.data.model

import android.net.Uri

data class ImageStorageDetails(
    val imageUri : Uri = Uri.EMPTY,
    val imagePath : String = "",
    val imageName : String = ""
)
