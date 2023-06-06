package com.aivanouski.example.imsearch.utils

import android.os.Build
import android.os.Parcelable
import androidx.fragment.app.Fragment

fun <T : Parcelable> Fragment.getMandatoryParcelableArgument(
    argumentName: String,
    type: Class<T>
): T {
    val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getParcelable(argumentName, type)
    } else {
        @Suppress("DEPRECATION")
        arguments?.getParcelable(argumentName) as? T
    }

    return value ?: throw IllegalStateException("Argument not found: $argumentName")
}