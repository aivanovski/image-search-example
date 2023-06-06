package com.aivanouski.example.imsearch.presentation.images.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aivanouski.example.imsearch.databinding.ItemImageBinding
import com.aivanouski.example.imsearch.presentation.images.recyclerView.ImagesAdapter.ImageItemViewHolder

class ImagesAdapter(
    context: Context
) : RecyclerView.Adapter<ImageItemViewHolder>() {

    private var items = emptyList<ImageItem>()
    private val inflater = LayoutInflater.from(context)

    fun updateItems(newItems: List<ImageItem>) {
        val diffCallback = ImagesAdapterDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.binding.executePendingBindings()
    }

    class ImageItemViewHolder(
        val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root)

    data class ImageItem(
        val id: Long,
        val username: String,
        val userImageUrl: String?,
        val tags: String,
        val url: String?
    ) {
        var onClicked: ((id: Long) -> Unit)? = null
    }
}