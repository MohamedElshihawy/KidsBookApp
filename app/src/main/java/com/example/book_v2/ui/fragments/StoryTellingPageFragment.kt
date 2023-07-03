package com.example.book.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.book_v2.data.oop.StoryTellingPage
import com.example.book_v2.databinding.StoryPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.loadImageIntoView
import com.example.book_v2.utilities.TextToSpeechSetUp
import java.util.*

class StoryTellingPageFragment(
    private val listener: PageNavListeners,
    private val pageData: StoryTellingPage
) :
    Fragment() {

    private lateinit var binding: StoryPageLayoutBinding
    private val storyParagraphs = ArrayList<String>()
    private lateinit var textToSpeech: TextToSpeech
    private var storySlideNum = 0
    private var paragraph = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = StoryPageLayoutBinding.inflate(layoutInflater, container, false)

        textToSpeech = TextToSpeechSetUp.newInstanceTTS(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpPage()
        setListeners()

    }


    private fun setUpPage() {

        val storyContent = pageData.storyContent.split(" ")

        storyContent.forEachIndexed { index, e ->
            paragraph += "$e "
            if (index > 0 && index % 52 == 0 || index == storyContent.size - 1) {
                storyParagraphs.add(paragraph)
                paragraph = ""
            }
        }

        binding.topOfPage.pageTitle.text = pageData.title
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        binding.storyContent.textDirection = View.TEXT_DIRECTION_ANY_RTL
        binding.storyContent.text = storyParagraphs[storySlideNum]
        binding.storyTitle.text = pageData.storyTitle
        binding.storyCover.loadImageIntoView(pageData.storyCoverPath)
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener {
            listener.nextPage()
        }
        binding.bottomOfPage.previousBtn.setOnClickListener {
            listener.previousPage()
        }

        binding.playParagraphBtn.setOnClickListener {
            if (textToSpeech.isSpeaking) {
                textToSpeech.stop()
            } else {
                textToSpeech.speak(
                    storyParagraphs[storySlideNum],
                    TextToSpeech.QUEUE_ADD,
                    null,
                    null
                )
            }
        }

        binding.nextParagraphBtn.setOnClickListener {
            storySlideNum++
            if (storySlideNum < storyParagraphs.size) {
                textToSpeech.stop()
                binding.storyContent.text = storyParagraphs[storySlideNum]
                textToSpeech.speak(
                    storyParagraphs[storySlideNum],
                    TextToSpeech.QUEUE_ADD,
                    null,
                    null
                )
            }
        }

        binding.previousParagraphBtn.setOnClickListener {
            storySlideNum--
            if (storySlideNum >= 0) {
                textToSpeech.stop()
                binding.storyContent.text = storyParagraphs[storySlideNum]
                textToSpeech.speak(
                    storyParagraphs[storySlideNum],
                    TextToSpeech.QUEUE_ADD,
                    null,
                    null
                )


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }


}