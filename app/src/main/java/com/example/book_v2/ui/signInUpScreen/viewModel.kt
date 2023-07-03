package com.example.book_v2.ui.signInUpScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.book_v2.data.models.CreateUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class viewModel(private val repository: Repository) : ViewModel() {

    private val createUser = MutableLiveData<String>()

    private val errorMessage = MutableLiveData<String>()


    fun createUser(user: CreateUser) {
        val response = repository.createUser(user)

        response.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    createUser.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        }
        )

    }




}