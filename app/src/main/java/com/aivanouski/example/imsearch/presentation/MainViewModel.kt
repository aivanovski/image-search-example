package com.aivanouski.example.imsearch.presentation

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router

class MainViewModel(
    private val router: Router
) : ViewModel() {

    fun navigateToMainScreen() {
        router.newRootScreen(Screens.ImagesScreen())
    }
}