package com.example.book.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.book_v2.data.adapters.ConstrainedMatchingAdapter
import com.example.book_v2.data.oop.ConstrainedMatchingPage
import com.example.book_v2.databinding.ConstrainedMatchingLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.SimpleItemDecoration
import com.example.book_v2.utilities.DrawLetters


class ConstrainedMatchingFragment(
    private val listener: PageNavListeners,
    private val pageData: ConstrainedMatchingPage
) : Fragment() {


    private lateinit var binding: ConstrainedMatchingLayoutBinding
    private lateinit var adapter: ConstrainedMatchingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConstrainedMatchingLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ConstrainedMatchingAdapter()
        setListeners()

        binding.leftLinesRcView.layoutManager =
            object : LinearLayoutManager(context) {
                override fun canScrollVertically() = false
            }
        binding.rightLinesRcView.layoutManager =
            object : LinearLayoutManager(context) {
                override fun canScrollVertically() = false
            }

        binding.rightLinesRcView.adapter = adapter
        binding.leftLinesRcView.adapter = adapter


        pageData.patternsPaths.forEach { dataItem ->
            val data = DrawLetters.getAllPointsFromFile(dataItem)
            pageData.patternsRepresentation.add(data)
        }

        binding.leftLinesRcView.addItemDecoration(SimpleItemDecoration())
        binding.rightLinesRcView.addItemDecoration(SimpleItemDecoration())

        adapter.addPage(pageData)

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener {
            listener.nextPage()
        }
        binding.bottomOfPage.previousBtn.setOnClickListener {
            listener.previousPage()
        }

        binding.viewCover.lock.setOnClickListener {
            binding.viewCover.root.visibility = View.GONE
        }

        binding.viewCover.root.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }
    }


}