package com.aivanouski.example.imsearch.presentation.imageDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageDetailsArguments(
    val imageId: Long
) : Parcelable