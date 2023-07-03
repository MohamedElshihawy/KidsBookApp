package com.example.book_v2.ui.signInUpScreen

import com.example.book_v2.data.models.CreateUser
import com.example.book_v2.data.models.Login
import com.example.book_v2.data.network.Client
import com.example.book_v2.data.network.api.SignInUp

class Repository(private val api: SignInUp) {


    fun createUser(user: CreateUser) = api.createUser(user)


    fun login(login: Login) = api.login(login)


}