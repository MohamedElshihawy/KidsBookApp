package com.example.book_v2.ui.fragments

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.R
import com.example.book_v2.data.database.ReportData
import com.example.book_v2.data.oop.LetterPronunciationPage
import com.example.book_v2.databinding.LetterPronunciationPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.utilities.DBOperations

class LetterPronunciationPageFragment(
    private val listener: PageNavListeners,
    private val pageData: LetterPronunciationPage,

) :
    Fragment(), RecognitionListener {

    val TAG = "letter pronunciation"

    private lateinit var binding: LetterPronunciationPageLayoutBinding
    private lateinit var speech: SpeechRecognizer
    private val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private var soundPlayer: MediaPlayer? = null
    private lateinit var blinkingAnimation: ObjectAnimator
    private var numOfTries = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LetterPronunciationPageLayoutBinding.inflate(layoutInflater, container, false)

        speech = SpeechRecognizer.createSpeechRecognizer(requireActivity())

        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { _ ->
                }
            }
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
            ),
        )
        speech.setRecognitionListener(this)
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG")
        recognizerIntent.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            requireContext().packageName,
        )
        recognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH,
        )

        recognizerIntent.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            requireContext().packageName,
        )

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPage()
        setListeners()
    }

    private fun setUpPage() {
        binding.topOfPage.pageTitle.text = pageData.title
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        binding.letterDisplayView.text = pageData.letterTxt
    }

    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener {
            listener.nextPage()
        }
        binding.bottomOfPage.previousBtn.setOnClickListener {
            listener.previousPage()
        }
        binding.speakingMicImage.setOnClickListener {
            binding.listeningNow.visibility = View.VISIBLE
            YoYo.with(Techniques.ZoomInUp)
                .duration(1000)
                .playOn(binding.listeningNow)
            speech.startListening(recognizerIntent)
        }
        binding.listeningNow.setOnClickListener {
            speech.stopListening()
        }
    }

    @Override
    override fun onResume() {
        super.onResume()
    }

    @Override
    override fun onPause() {
        super.onPause()
        speech.destroy()
        soundPlayer?.release()
    }

    override fun onDestroy() {
        speech.destroy()
        soundPlayer?.release()
        super.onDestroy()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.e(TAG, "onReadyForSpeech: ready for speech")
    }

    override fun onBeginningOfSpeech() {
        Log.e(TAG, "onBeginningOfSpeech: begin speech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.e(TAG, "onRmsChanged: changed")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.e(TAG, "onBufferReceived: received")
    }

    override fun onEndOfSpeech() {
        Log.e(TAG, "onEndOfSpeech: speech ended")
        binding.listeningNow.visibility = View.GONE
    }

    override fun onError(error: Int) {
        Log.e(TAG, "onError: error occurred   ${getErrorText(error)}")
        Toast.makeText(
            requireContext(),
            "something went wrong please check your internet connection or try again later ",
            Toast.LENGTH_SHORT,
        ).show()
        numOfTries++
        binding.listeningNow.visibility = View.GONE
    }

    override fun onResults(results: Bundle?) {
        val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        matches?.get(0)?.let { match ->
            if (match == pageData.letterPronunciation || match == pageData.alternativeLetterPronunciation) {
                soundPlayer = MediaPlayer.create(requireContext(), R.raw.right_answer_sound)
                blinkingAnimation =
                    ObjectAnimator.ofInt(
                        binding.letterDisplayView,
                        "backgroundColor",
                        Color.WHITE,
                        Color.GREEN,
                        Color.WHITE,

                    )
                context?.let {
                    DBOperations.storeReport(
                        it,
                        ReportData(
                            pageData.pageNum.toInt(),
                            pageData.letterTxt,
                            100.0,
                        ),
                    )
                }
            } else {
                soundPlayer = MediaPlayer.create(requireContext(), R.raw.wrong_answer_sound)
                blinkingAnimation =
                    ObjectAnimator.ofInt(
                        binding.letterDisplayView,
                        "backgroundColor",
                        Color.WHITE,
                        Color.RED,
                        Color.WHITE,
                    )
            }
            blinkingAnimation.duration = 500
            blinkingAnimation.setEvaluator(ArgbEvaluator())
            blinkingAnimation.repeatCount = 5
            blinkingAnimation.repeatMode = ValueAnimator.REVERSE
            soundPlayer?.start()
            blinkingAnimation.start()
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.e(TAG, "onEvent:$eventType ")
    }

    private fun getErrorText(errorCode: Int): String {
        val message: String = when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Didn't understand, please try again."
        }
        return message
    }
}
