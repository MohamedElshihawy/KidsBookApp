package com.example.book_v2.utilities // ktlint-disable package-name

import android.content.Context
import android.util.Log
import com.example.book_v2.data.database.ReportData
import com.example.book_v2.data.database.database

object DBOperations {
    fun storeReport(context: Context, reportData: ReportData) {
        val db = database(context)
        val res = db.insert(reportData)
        if (res) {
            Log.e("TAG", "insert  : ")
        } else {
            Log.e("TAG", "update : ")
            db.updateItem(reportData)
        }
    }
}
