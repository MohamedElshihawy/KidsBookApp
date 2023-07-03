package com.example.book_v2.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.book_v2.ui.signInUpScreen.Repository
import com.example.book_v2.data.network.api.SignInUp
import com.example.book_v2.ui.signInUpScreen.viewModel

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
class MyViewModelFactory : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        viewModel::class -> viewModel(repository = Repository(api = SignInUp.getInstance()))
        else -> {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    } as T


}