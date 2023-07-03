package com.example.book_v2.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {

    var retrofit1: Retrofit? = null
    var retrofit2: Retrofit? = null

    fun init1(): Retrofit {
        if (retrofit1 == null) {
            retrofit1 = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit1!!
    }

    fun init2(): Retrofit {
        if (retrofit2 == null) {
            retrofit2 = Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit2!!
    }


}