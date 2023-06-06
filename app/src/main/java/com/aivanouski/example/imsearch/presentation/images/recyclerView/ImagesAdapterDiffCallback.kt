package com.aivanouski.example.imsearch.presentation.images.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImagesAdapter.ImageItem

class ImagesAdapterDiffCallback(
    private val oldItems: List<ImageItem>,
    private val newItems: List<ImageItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}