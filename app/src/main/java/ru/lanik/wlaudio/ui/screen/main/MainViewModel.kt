package ru.lanik.wlaudio.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class MainViewModel(
    private val navController: NavController,
) : ViewModel() {
    fun navigateTo(
        resId: Int,
    ) {
        navController.navigate(resId)
    }
}