package com.example.book_v2.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.book_v2.data.models.Circle
import com.example.book_v2.data.oop.ConstrainedMatchingPage
import com.example.book_v2.databinding.ConstrainedMatchingItemBinding
import com.example.book_v2.utilities.DrawLetters
import java.util.ArrayList

class ConstrainedMatchingAdapter() :
    RecyclerView.Adapter<ConstrainedMatchingAdapter.MyViewHolder>() {

    private var firstItem = true

    private var pageData = ConstrainedMatchingPage()

    inner class MyViewHolder(private val binding: ConstrainedMatchingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun adjustView(position: Int) {
            val data = pageData.patternsRepresentation[position]

            binding.itemView.setCircleList(data)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item = ConstrainedMatchingItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val params = parent.layoutParams
        params.height = (((parent.height - (parent.height * .10)) / pageData.numOfPatterns)).toInt()
        params.width = (parent.width * .95).toInt()

        item.root.layoutParams = params
        if (firstItem) {
            pageData.patternsRepresentation.forEachIndexed { index, dataItem ->
                val data = DrawLetters.ShiftPointsInScreen(
                    params.width,
                    params.height,
                    dataItem as ArrayList<Circle>
                )
                pageData.patternsRepresentation[index] = data
            }
            firstItem = false
        }

        return MyViewHolder(item)
    }

    override fun getItemCount(): Int {

        return pageData.patternsRepresentation.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.adjustView(position)
    }


    fun addPage(page: ConstrainedMatchingPage) {
        pageData = page
        notifyDataSetChanged()
    }

}