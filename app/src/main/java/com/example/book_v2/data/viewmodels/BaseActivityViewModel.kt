package com.example.book_v2.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.book_v2.utilities.ReadFiles


class BaseActivityViewModel
    : ViewModel() {

    var filesMetaDataLiveData = MutableLiveData<List<String>>()


    fun getMetaDataOfAllPages(Path: String?) {
        val filesMetaData: List<String> =
            ReadFiles.getMetaDataOfAllPages(ReadFiles.getAllLettersFilesPaths(Path))
        filesMetaDataLiveData.postValue(filesMetaData)
    }
}