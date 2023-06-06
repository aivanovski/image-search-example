package com.aivanouski.example.imsearch.presentation.core

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.aivanouski.example.imsearch.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("isVisible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter(value = ["imageUrl", "placeholder", "placeholderViewId"], requireAll = false)
fun setGlideImage(
    imageView: ImageView,
    imageUrl: String?,
    placeholder: Drawable?,
    @IdRes placeholderViewId: Int?
) {
    val placeholderView = placeholderViewId?.let {
        findPlaceholderView(imageView, placeholderViewId)
    }

    imageView.setImageDrawable(null)
    if (!imageUrl.isNullOrEmpty()) {
        placeholderView?.isVisible = true

        val builder = Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(placeholder)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    placeholderView?.isVisible = false
                    return false
                }
            })

        builder.into(imageView)
    } else {
        if (placeholder != null) {
            imageView.setImageDrawable(placeholder)
        }

        placeholderView?.isVisible = false
    }
}

private fun findPlaceholderView(
    view: View,
    @IdRes placeholderViewId: Int
): View {
    return (view.parent as ViewGroup).findViewById(placeholderViewId)
        ?: throw IllegalArgumentException()
}

@BindingAdapter("onTextChanged")
fun setOnTextChangedListener(
    editText: TextInputEditText,
    onTextChangeListener: OnTextChangeListener?
) {
    val existingListener = editText.getTag(R.id.tagTextWatcher) as? TextWatcher
    existingListener?.let {
        editText.removeTextChangedListener(it)
    }

    if (onTextChangeListener == null) {
        return
    }

    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            onTextChangeListener.onTextChanged(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    editText.setTag(R.id.tagTextWatcher, listener)
    editText.addTextChangedListener(listener)
}