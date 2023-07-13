package com.example.book_v2.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.R
import com.example.book_v2.data.oop.ColoringPage
import com.example.book_v2.databinding.ColoringPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.uriToBitmap
import com.example.book_v2.ui.activities.BookCoverActivity
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.coroutines.*

class ColoringPageFragment(
    private var listener: PageNavListeners,
    private val pageData: ColoringPage,
) : Fragment() {

    private lateinit var binding: ColoringPageLayoutBinding
    private var isPenBarOpen = false
    private var isToolBarOpen = false
    private var strokeSize = 10
    private var strokeColor = Color.BLACK

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ColoringPageLayoutBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPage()
        setListeners()

        binding.imageToBeColored.post {
            GlobalScope.launch(Dispatchers.IO) {
                val coloredImage = async {
                    uriToBitmap(
                        requireContext(),
                        pageData.filledImagePath,
                        binding.coloredImage.width,
                        binding.coloredImage.height,
                    )
                }
                val imageToBeColored = async {
                    uriToBitmap(
                        requireContext(),
                        pageData.outlinedImagePath,
                        binding.imageToBeColored.width,
                        binding.imageToBeColored.height,
                    )
                }

                withContext(Dispatchers.Main) {
                    val image = imageToBeColored.await().copy(Bitmap.Config.ARGB_8888, true)
                    binding.imageToBeColored.setBitmapInCanvas(image)
                    binding.imageToBeColored.invalidate()
                    binding.coloredImage.setImageBitmap(coloredImage.await())
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener { listener.nextPage() }
        binding.bottomOfPage.previousBtn.setOnClickListener { listener.previousPage() }

        binding.viewCover.lock.setOnClickListener {
            binding.viewCover.root.visibility = View.GONE
        }

        binding.viewCover.root.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }

        binding.toolIcon.setOnClickListener {
            if (!isToolBarOpen) {
                binding.toolBar.root.visibility = View.VISIBLE
                YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(binding.toolBar.root)
                isToolBarOpen = true
            } else {
                binding.toolBar.root.visibility = View.GONE
                isToolBarOpen = false
            }
        }

        binding.toolBar.pen.setOnClickListener {
            if (!isPenBarOpen) {
                binding.toolBar.penBar.visibility = View.VISIBLE
                YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(binding.toolBar.penBar)
                binding.toolBar.pen.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
                isPenBarOpen = true
            } else {
                binding.toolBar.penBar.visibility = View.GONE
                binding.toolBar.pen.setBackgroundColor(resources.getColor(R.color.light_grey))
                isPenBarOpen = false
            }
        }
        binding.toolBar.brush.setOnClickListener {
            ColorSheet().colorPicker(
                colors = BookCoverActivity.colorArr,
                noColorOption = true,
                listener = { color ->
                    strokeColor = color
                    binding.imageToBeColored.currentStrokeColor = strokeColor
                },
            )
                .show(requireActivity().supportFragmentManager)
            strokeSize = 48
            binding.imageToBeColored.currentStrokeWidth = strokeSize
        }

        binding.toolBar.undo.setOnClickListener {
            binding.imageToBeColored.undoLastAction()
        }

        binding.toolBar.redo.setOnClickListener {
            binding.imageToBeColored.redoLastAction()
        }

        binding.toolBar.colorPallete.setOnClickListener {
            ColorSheet().colorPicker(
                colors = BookCoverActivity.colorArr,
                noColorOption = true,
                listener = { color ->
                    strokeColor = color
                    binding.imageToBeColored.currentStrokeColor = strokeColor
                },
            )
                .show(requireActivity().supportFragmentManager)
        }

        binding.toolBar.smallFont.setOnClickListener {
            strokeSize = 12
            binding.imageToBeColored.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }
        binding.toolBar.mediumFont.setOnClickListener {
            strokeSize = 20
            binding.imageToBeColored.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }
        binding.toolBar.largeFont.setOnClickListener {
            strokeSize = 32
            binding.imageToBeColored.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }
    }

    private fun setUpPage() {
        binding.topOfPage.pageTitle.text = pageData.title
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        binding.imageToBeColored.currentStrokeColor = strokeColor
        binding.imageToBeColored.currentStrokeWidth = strokeSize
    }

    override fun onPause() {
        super.onPause()
        binding.viewCover.root.visibility = View.VISIBLE
    }

    fun getItemId(): Long {
        // Return a unique ID for this fragment
        return this.hashCode().toLong()
    }
}
