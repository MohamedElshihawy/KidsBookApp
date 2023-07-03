package com.example.book_v2.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.example.book_v2.data.oop.IntroductionPage
import com.example.book_v2.databinding.IntroductionPageLayoutBinding
import com.example.book_v2.services.interfaces.ChangePageTheme
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.loadImageIntoView
import com.example.book_v2.services.setHighLightedText
import com.example.book_v2.services.uriToBitmap
import com.example.book_v2.utilities.TextToSpeechSetUp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


class IntroductionPageFragment(
    private val listener: PageNavListeners,
    private val pageData: IntroductionPage
) : Fragment(), ChangePageTheme {

    private lateinit var binding: IntroductionPageLayoutBinding
    private lateinit var textToSpeech: TextToSpeech
    private val pageTheme: ChangePageTheme = this

    val TAG = "Introduction page"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = IntroductionPageLayoutBinding.inflate(layoutInflater, container, false)

        MainScope().launch {
            Palette.from(
                uriToBitmap(
                    requireContext(), pageData.animalImagePath
                )
            ).generate { generated ->
//                if (generated != null) {
//                    pageTheme.onColorsLoaded(
//                        generated.swatches[Random()
//                            .nextInt(generated.swatches.size - 1)]
//                    )
//                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textToSpeech = TextToSpeechSetUp.newInstanceTTS(requireContext())
        setUpPage()
        setListeners()

    }


    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener { listener.nextPage() }
        binding.bottomOfPage.previousBtn.setOnClickListener { listener.previousPage() }
        binding.mic.setOnClickListener {
            if (textToSpeech.isSpeaking) {
                textToSpeech.stop()
            }
            MainScope().launch {
                textToSpeech.speak(pageData.letterText, TextToSpeech.QUEUE_ADD, null, null)
                delay(1000)
                textToSpeech.speak(pageData.animalName, TextToSpeech.QUEUE_ADD, null, null)
            }
        }
    }

    private fun setUpPage() {
        binding.animalImage.loadImageIntoView(pageData.animalImagePath)
        binding.animalName.text = pageData.animalName
        binding.letterTxt.text = pageData.letterText
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
    }

    override fun onColorsLoaded(palette: Palette.Swatch) {
        var mainColor = palette.rgb
        // mainColor = adjustAlpha(mainColor, .3f)
        binding.root.setBackgroundColor(mainColor)
        binding.topOfPage.pageTitle.setTextColor(palette.bodyTextColor)
        binding.letterTxt.setTextColor(palette.bodyTextColor)
        binding.animalName.setTextColor(palette.bodyTextColor)
//        binding.topOfPage.leftHalfPageSeparator.setColorFilter(
//            palette.rgb,
//            PorterDuff.Mode.SRC_ATOP
//        )
//        binding.topOfPage.rightHalfPageSeparator.setColorFilter(
//            palette.rgb,
//            PorterDuff.Mode.SRC_ATOP
//        )
        setHighLightedText(
            binding.animalName, pageData.letterText, palette.titleTextColor
        )
        binding.cloud.setColorFilter(palette.rgb)
    }

    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt().toInt()
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

}