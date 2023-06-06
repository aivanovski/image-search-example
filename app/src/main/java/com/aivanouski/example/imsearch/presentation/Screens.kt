package com.aivanouski.example.imsearch.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.aivanouski.example.imsearch.presentation.imageDetails.ImageDetailsArguments
import com.aivanouski.example.imsearch.presentation.imageDetails.ImageDetailsFragment
import com.aivanouski.example.imsearch.presentation.images.ImagesFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    class ImagesScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            ImagesFragment.newInstance()
    }

    class ImageDetailsScreen(
        val arguments: ImageDetailsArguments
    ) : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            ImageDetailsFragment.newInstance(arguments)
    }
}