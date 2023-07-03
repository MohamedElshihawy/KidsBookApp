package com.example.book_v2.data.network.api

import com.example.book_v2.data.models.CreateUser
import com.example.book_v2.data.models.Login
import com.example.book_v2.data.network.Client
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInUp {


    @POST("users/")
    fun createUser(@Body user: CreateUser): Call<String>

    @POST("users/login")
    fun login(@Body login: Login): Call<String>


    companion object {
        var user: SignInUp? = null

        fun getInstance(): SignInUp {
            if (user == null) {
                val retrofitInit = Client.init1()
                user = retrofitInit.create(SignInUp::class.java)
            }
            return user!!
        }
    }

}