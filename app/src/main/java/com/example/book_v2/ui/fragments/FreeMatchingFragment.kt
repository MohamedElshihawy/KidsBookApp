package com.example.book.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.book_v2.data.adapters.FreeMatchingAdapter
import com.example.book_v2.data.oop.FreeMatchingPage
import com.example.book_v2.databinding.FreeMatchingPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners

class FreeMatchingFragment(
    private val listener: PageNavListeners,
    private val pageData: FreeMatchingPage
) : Fragment() {

    private lateinit var binding: FreeMatchingPageLayoutBinding
    private lateinit var linesListData: List<FreeMatchingPage>
    private lateinit var adapter: FreeMatchingAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FreeMatchingPageLayoutBinding.inflate(inflater, container, false)


        linesListData = MutableList(pageData.numOfLines) { pageData }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FreeMatchingAdapter()
        adapter.addLines(linesListData)

        setListeners()


        val layout1 = if (pageData.viewType.equals("GridLayout", true)) {
            object : GridLayoutManager(context, pageData.numOfColumns) {
                override fun canScrollVertically() = false
                override fun canScrollHorizontally() = false
            }
        } else {
            if (pageData.orientation.equals(ORIENTATION_VERTICAL, true)) {
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically() = false
                    override fun setOrientation(orientation: Int) {
                        super.setOrientation(VERTICAL)
                    }
                }
            } else {
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically() = false
                    override fun setOrientation(orientation: Int) {
                        super.setOrientation(HORIZONTAL)
                    }
                }
            }
        }

        val layout2 = if (pageData.viewType.equals("GridLayout", true)) {
            object : GridLayoutManager(context, pageData.numOfColumns) {
                override fun canScrollVertically() = false
                override fun canScrollHorizontally() = false
            }
        } else {
            if (pageData.orientation.equals(ORIENTATION_VERTICAL, true)) {
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically() = false
                    override fun setOrientation(orientation: Int) {
                        super.setOrientation(VERTICAL)
                    }
                }
            } else {
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically() = false
                    override fun setOrientation(orientation: Int) {
                        super.setOrientation(HORIZONTAL)
                    }
                }
            }
        }


        binding.leftLinesRcView.layoutManager = layout1
        binding.rightLinesRcView.layoutManager = layout2

        binding.rightLinesRcView.adapter = adapter
        binding.leftLinesRcView.adapter = adapter
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


    companion object {
        const val ORIENTATION_VERTICAL = "Vertical"
        const val ORIENTATION_HORIZONTAL = "Horizontal"
    }


    override fun onPause() {
        super.onPause()
        binding.viewCover.root.visibility = View.VISIBLE
    }
}