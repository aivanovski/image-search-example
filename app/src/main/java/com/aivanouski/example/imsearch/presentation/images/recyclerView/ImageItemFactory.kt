package com.aivanouski.example.imsearch.presentation.images.recyclerView

import com.aivanouski.example.imsearch.model.Image

class ImageItemFactory {

    fun createAdapterItems(
        images: List<Image>,
        onImageClicked: (image: Image) -> Unit,
        isUseImages: Boolean
    ): List<ImagesAdapter.ImageItem> {
        val imageMap = images.associateBy { image -> image.id }
        val onItemClicked = { id: Long ->
            val image = imageMap[id]
            if (image != null) {
                onImageClicked.invoke(image)
            }
        }

        return images.map { image ->
            ImagesAdapter.ImageItem(
                id = image.id,
                username = image.username,
                userImageUrl = image.userImageUrl.ifEmpty { null },
                tags = image.tags,
                url = if (isUseImages) image.previewUrl else null
            ).apply {
                onClicked = onItemClicked
            }
        }
    }
}