package com.aivanouski.example.imsearch.presentation.imageDetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aivanouski.example.imsearch.databinding.ImageDetailsFragmentBinding
import com.aivanouski.example.imsearch.utils.getMandatoryParcelableArgument
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoViewAttacher

class ImageDetailsFragment : Fragment() {

    private val args: ImageDetailsArguments by lazy {
        getMandatoryParcelableArgument(ARGUMENTS, ImageDetailsArguments::class.java)
    }

    private val viewModel: ImageDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            ImageDetailsViewModel.Factory(args)
        )[ImageDetailsViewModel::class.java]
    }

    private lateinit var binding: ImageDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ImageDetailsFragmentBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
            }

        binding.toolbar.setNavigationOnClickListener { viewModel.navigateBack() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.url.observe(viewLifecycleOwner) { url ->
            onLoadImageUrl(url)
        }
    }

    private fun onLoadImageUrl(url: String) {
        binding.imagePlaceholder.isVisible = true
        binding.image.setImageDrawable(null)

        Glide.with(requireContext())
            .load(url)
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
                    if (resource != null) {
                        binding.imagePlaceholder.isVisible = false
                        onImageLoaded(resource)
                    }

                    return false
                }
            })
            .into(binding.image)
    }

    private fun onImageLoaded(image: Drawable) {
        val attacher = PhotoViewAttacher(binding.image)

        val (minZoom, midZoom, maxZoom) = calculateZoomLevels(
            imageWidth = image.intrinsicWidth,
            imageHeight = image.intrinsicHeight,
            imageViewWidth = binding.image.width,
            imageViewHeight = binding.image.height
        )

        attacher.setScaleLevels(minZoom, midZoom, maxZoom)

        binding.image.post {
            attacher.setScale(minZoom, false)
        }
    }

    private fun calculateZoomLevels(
        imageWidth: Int,
        imageHeight: Int,
        imageViewWidth: Int,
        imageViewHeight: Int
    ): Triple<Float, Float, Float> {
        val minZoom = when {
            imageWidth > imageHeight -> {
                imageViewWidth.toFloat() / imageWidth
            }

            imageWidth < imageHeight -> {
                imageViewHeight.toFloat() / imageHeight / 2
            }

            else -> 1f
        }

        return Triple(minZoom, minZoom * 2, minZoom * 4)
    }

    companion object {

        private const val ARGUMENTS = "arguments"

        fun newInstance(args: ImageDetailsArguments): ImageDetailsFragment {
            val argsBundle = Bundle()
                .apply {
                    putParcelable(ARGUMENTS, args)
                }

            return ImageDetailsFragment()
                .apply {
                    arguments = argsBundle
                }
        }
    }
}