package ru.lanik.wlaudio.ui.screen.main

import androidx.navigation.NavController

class MainViewModelFactory {
    fun getViewModel(
        navController: NavController,
    ): MainViewModel {
        return MainViewModel(
            navController = navController,
        )
    }
}