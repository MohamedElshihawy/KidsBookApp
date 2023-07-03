package com.example.book_v2.data.models


data class CreateUser(
    var firstName: String,
    var lastName: String,
    var password: String,
    var parentEmail: String,
    var age: Int,
    var gender: Int
)
