package com.example.book_v2.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.book_v2.data.oop.FreeMatchingPage
import com.example.book_v2.databinding.FreeMatchingItemBinding

class FreeMatchingAdapter : RecyclerView.Adapter<FreeMatchingAdapter.MyViewHolder>() {

    private val smallViewHorizontal = 0
    private val smallViewVertical = 1
    private val mediumViewVertical = 2
    private val mediumViewHorizontal = 3
    private val largeViewVertical = 4
    private val largeViewHorizontal = 5

    private var lineShapes = ArrayList<FreeMatchingPage>()

    inner class MyViewHolder(private val binding: FreeMatchingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun adjustViews(matchingPage: FreeMatchingPage) {
            binding.itemView.viewSize = matchingPage.viewSize
            binding.itemView.lineShape = matchingPage.lineType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item = FreeMatchingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        when (viewType) {
            smallViewVertical, smallViewHorizontal -> {
                item.itemView.layoutParams.width = parent.width / 4
                item.itemView.layoutParams.height = parent.height / 4
            }
            mediumViewVertical -> {
                item.itemView.layoutParams.width = parent.width / 6
                item.itemView.layoutParams.height = parent.height / 2
            }
            mediumViewHorizontal -> {
                item.itemView.layoutParams.width = parent.width / 2
                item.itemView.layoutParams.height = parent.height / 6
            }
            largeViewHorizontal, largeViewVertical -> {
                item.itemView.layoutParams.width = parent.width
                item.itemView.layoutParams.height = parent.height / 6
            }
        }

        return MyViewHolder(item)
    }

    override fun getItemCount(): Int {
        return lineShapes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.adjustViews(lineShapes[position])
    }


    override fun getItemViewType(position: Int): Int {
        val page = lineShapes[position]

        if (page.viewSize.equals(
                "Small Matching View",
                true
            ) && page.orientation.equals("Vertical", true)
        ) {
            return smallViewVertical
        } else if (page.viewSize.equals(
                "smallMatchingView",
                true
            ) && page.orientation.equals("Horizontal", true)
        ) {
            return smallViewHorizontal
        } else if (page.viewSize.equals(
                "Medium Matching View",
                true
            ) && page.orientation.equals("Vertical", true)
        ) {
            return mediumViewVertical
        } else if (page.viewSize.equals(
                "Medium Matching View",
                true
            ) && page.orientation.equals("Horizontal", true)
        ) {
            return mediumViewHorizontal
        } else if (page.viewSize.equals(
                "Large Matching View",
                true
            ) && page.orientation.equals("Vertical", true)
        ) {
            return largeViewVertical
        } else if (page.viewSize.equals(
                "Large Matching View",
                true
            ) && page.orientation.equals("Horizontal", true)
        ) {
            return largeViewHorizontal
        }
        return 0
    }

    fun addLines(linesList:List<FreeMatchingPage>){
        lineShapes.addAll(linesList)
        notifyDataSetChanged()
    }


}