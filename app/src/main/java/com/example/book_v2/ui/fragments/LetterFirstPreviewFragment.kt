package com.example.book.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.R
import com.example.book_v2.data.oop.LetterFirstPreviewPage
import com.example.book_v2.databinding.LetterFirstPreviewPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.setHighLightedText
import com.example.book_v2.ui.activities.BookCoverActivity.Companion.colorArr
import com.example.book_v2.utilities.TextToSpeechSetUp
import dev.sasikanth.colorsheet.ColorSheet

class LetterFirstPreviewFragment(
    private val listener: PageNavListeners,
    private val pageData: LetterFirstPreviewPage
) : Fragment() {

    private lateinit var binding: LetterFirstPreviewPageLayoutBinding
    private lateinit var textToSpeech: TextToSpeech
    private var isPenBarOpen = false
    private var isToolBarOpen = false
    private var strokeSize = 10
    private var strokeColor = Color.BLACK

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LetterFirstPreviewPageLayoutBinding.inflate(layoutInflater, container, false)

        textToSpeech = TextToSpeechSetUp.newInstanceTTS(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setListeners()
        setUpPage()
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

        binding.mic.setOnClickListener {
            textToSpeech.speak(pageData.letterText, TextToSpeech.QUEUE_ADD, null, null)
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
                colors = colorArr,
                noColorOption = true,
                listener = { color ->
                    strokeColor = color
                    binding.writeTextView.currentStrokeColor = strokeColor

                })
                .show(requireActivity().supportFragmentManager)
            strokeSize = 48
            binding.writeTextView.currentStrokeWidth = strokeSize

        }

        binding.toolBar.undo.setOnClickListener {
            binding.writeTextView.undoLastAction()
        }

        binding.toolBar.redo.setOnClickListener {
            binding.writeTextView.redoLastAction()
        }

        binding.toolBar.colorPallete.setOnClickListener {
            ColorSheet().colorPicker(
                colors = colorArr,
                noColorOption = true,
                listener = { color ->
                    strokeColor = color
                    binding.writeTextView.currentStrokeColor = strokeColor

                })
                .show(requireActivity().supportFragmentManager)
        }

        binding.toolBar.smallFont.setOnClickListener {
            strokeSize = 12
            binding.writeTextView.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }
        binding.toolBar.mediumFont.setOnClickListener {
            strokeSize = 20
            binding.writeTextView.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }
        binding.toolBar.largeFont.setOnClickListener {
            strokeSize = 32
            binding.writeTextView.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }


    }

    private fun setUpPage() {
        binding.animalName.text = pageData.wordText
        binding.writeTextView.setText(pageData.letterText)
        binding.topOfPage.pageTitle.text = pageData.title
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        setHighLightedText(binding.animalName, pageData.letterText, Color.BLACK)
        binding.writeTextView.currentStrokeColor = strokeColor
        binding.writeTextView.currentStrokeWidth = strokeSize
    }


    override fun onPause() {
        super.onPause()
        binding.viewCover.root.visibility = View.VISIBLE
    }

}