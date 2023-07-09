package com.example.book_v2.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.book_v2.data.adapters.ReportAdapter
import com.example.book_v2.data.database.ReportData
import com.example.book_v2.databinding.ReportLayoutBinding
import com.example.book_v2.utilities.DBOperations
import com.example.book_v2.utilities.GridSpacingItemDecoration

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ReportLayoutBinding
    private var allData: ArrayList<ReportData> = ArrayList()
    private lateinit var adapter: ReportAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReportLayoutBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }

        allData = DBOperations.getAllReports(context = this)
        adapter = ReportAdapter(allData)
        adapter.notifyDataSetChanged()
        binding.rv.layoutManager = GridLayoutManager(this, 3)
        binding.rv.addItemDecoration(
            GridSpacingItemDecoration(
                spacing = 16,
                spanCount = 3,
                includeEdge = true,
            ),
        )
        binding.rv.adapter = adapter
        binding.topOfPage.pageTitle.text = "التقييم"
        binding.backButton.setOnClickListener { this.finish() }
    }
}
