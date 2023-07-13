package com.example.book_v2.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.R
import com.example.book_v2.data.database.ReportData
import com.example.book_v2.data.oop.*
import com.example.book_v2.databinding.LargeLetterPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.interfaces.TaskCompletionListener
import com.example.book_v2.services.loadImageIntoView
import com.example.book_v2.services.setHighLightedText
import com.example.book_v2.services.uriToBitmap
import com.example.book_v2.ui.Effects.Animation
import com.example.book_v2.ui.activities.BookCoverActivity.Companion.colorArr
import com.example.book_v2.utilities.DBOperations
import com.example.book_v2.utilities.DrawLetters
import com.example.book_v2.utilities.TextToSpeechSetUp
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class LargeLetterFragment(
    private val listener: PageNavListeners,
    private val pageData: LargeLetterPage,
) : Fragment(), TaskCompletionListener {

    private lateinit var binding: LargeLetterPageLayoutBinding
    private lateinit var textToSpeech: TextToSpeech
    private var isPenBarOpen = false
    private var isToolBarOpen = false
    private var strokeSize = 10
    private var currentPanel = -1
    private var completedViews = 0
    private var totalAccuracy = 0.0
    private var tt = 0.0
    private var strokeColor = Color.BLACK
    private lateinit var swatches: List<Palette.Swatch>
    private var arr = IntArray(20)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LargeLetterPageLayoutBinding.inflate(layoutInflater, container, false)

        textToSpeech = TextToSpeechSetUp.newInstanceTTS(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        setUpPage()

        val largeLetterData = DrawLetters.getAllPointsFromFile(pageData.largeLetterIndexesFile)

        binding.letterView1.post {
            val final = DrawLetters.ShiftPointsInScreen(
                binding.letterView1.width,
                binding.letterView1.height,
                largeLetterData,
            )
            binding.letterView2.setCircleList(final)
            binding.letterView1.setCircleList(final)
            pageData.largeLetterRepresentation = final

            MainScope().launch {
                Palette.from(uriToBitmap(requireContext(), pageData.animalImagePath))
                    .generate { generated ->
                        if (generated != null) {
                            swatches = generated.swatches
//                            Log.e("TAG", "generateColors: success obtained colors")
//                            swatches.forEachIndexed { index, e ->
//                                arr[e.] = e.rgb
//
//                            }
//                            arr[0] = generated.lightVibrantSwatch!!.rgb
//                            arr[1] = generated.lightVibrantSwatch!!.bodyTextColor
//                            arr[2] = generated.lightVibrantSwatch!!.titleTextColor
                        }
                    }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener {
            listener.nextPage()
            Log.e("TAG", "onTaskCompleted: $totalAccuracy")
        }
        binding.bottomOfPage.previousBtn.setOnClickListener { listener.previousPage() }

        binding.micTestPage.setOnClickListener {
            textToSpeech.speak(pageData.letter, TextToSpeech.QUEUE_ADD, null, null)
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

        binding.letterView1.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 0
            }
            return@setOnTouchListener false
        }

        binding.letterView2.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 1
            }
            return@setOnTouchListener false
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
                    binding.letterView1.currentStrokeColor = strokeColor
                    binding.letterView2.currentStrokeColor = strokeColor
                },
            )
                .show(requireActivity().supportFragmentManager)

            strokeSize = 24
            binding.letterView1.currentStrokeWidth = strokeSize
            binding.letterView2.currentStrokeWidth = strokeSize
        }

        binding.toolBar.undo.setOnClickListener {
            when (currentPanel) {
                0 -> {
                    binding.letterView1.undoLastAction()
                }

                1 -> {
                    binding.letterView2.undoLastAction()
                }

                else -> {
                    Toast.makeText(requireContext(), "nothing to undo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.toolBar.redo.setOnClickListener {
            when (currentPanel) {
                0 -> {
                    binding.letterView1.redoLastAction()
                }

                1 -> {
                    binding.letterView2.redoLastAction()
                }

                else -> {
                    Toast.makeText(requireContext(), "nothing to redo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.toolBar.colorPallete.setOnClickListener {
            ColorSheet().colorPicker(
                colors = arr,
                noColorOption = true,
                listener = { color ->
                    strokeColor = color
                    binding.letterView1.currentStrokeColor = strokeColor
                    binding.letterView2.currentStrokeColor = strokeColor
                },
            )
                .show(requireActivity().supportFragmentManager)
        }

        binding.toolBar.smallFont.setOnClickListener {
            strokeSize = 4
            binding.letterView1.currentStrokeWidth = strokeSize
            binding.letterView2.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.toolBar.mediumFont.setOnClickListener {
            strokeSize = 8
            binding.letterView1.currentStrokeWidth = strokeSize
            binding.letterView2.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.toolBar.largeFont.setOnClickListener {
            strokeSize = 16
            binding.letterView1.currentStrokeWidth = strokeSize
            binding.letterView2.currentStrokeWidth = strokeSize
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.pen.setOnClickListener {
            binding.letterView1.disableFingerTouch = true
            binding.letterView2.disableFingerTouch = true
        }
    }

    private fun setUpPage() {
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        binding.topOfPage.pageTitle.text = pageData.title
        binding.animalImage.loadImageIntoView(pageData.animalImagePath)
        binding.letterTxt.text = pageData.letter
        setHighLightedText(
            binding.letterTxt,
            pageData.letter,
            colorArr[Random.nextInt(colorArr.size - 1)],
        )

        binding.letterView1.currentStrokeColor = strokeColor
        binding.letterView1.currentStrokeWidth = strokeSize

        binding.letterView2.currentStrokeColor = strokeColor
        binding.letterView2.currentStrokeWidth = strokeSize

        binding.letterView1.completionListener = this
        binding.letterView2.completionListener = this
    }

    override fun onTaskCompleted(accuracy: Double) {
        if (currentPanel == 0) {
            binding.letterView1.touchEnabled = false
        } else if (currentPanel == 1) {
            binding.letterView2.touchEnabled = false
        }

        completedViews++
        totalAccuracy = +accuracy
        if (completedViews == 2) {
            totalAccuracy /= 2
            val fragment = PageCompletedFragment(totalAccuracy)
            fragment.show(parentFragmentManager, "fragment")
            val celebrate = Animation.getInstance()
            celebrate.drawable = ResourcesCompat.getDrawable(resources, R.drawable.star, null)
            binding.celebrationView.start(celebrate.startRainAnimation())
            context?.let {
                DBOperations.storeReport(
                    it,
                    ReportData(
                        pageData.pageNum.toInt(),
                        pageData.letter,
                        totalAccuracy,
                    ),
                )
            }
        }
    }

    override fun onOutOfPathLineDetected(x: Float, y: Float, newScore: Double) {
        binding.userProgress.progress = (newScore / 2).toInt()
        when (currentPanel) {
            0 -> {
                binding.letterView1.handleInaccurateHandWriting(x, y)
            }

            1 -> {
                binding.letterView2.handleInaccurateHandWriting(x, y)
            }
        }
    }

    override fun onProgressAchieved(progress: Double, overAll: Int) {
        binding.userProgress.max = overAll * 2
        binding.userProgress.progress = (progress + totalAccuracy).toInt()
    }
}
