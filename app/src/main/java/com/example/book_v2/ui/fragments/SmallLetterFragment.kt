package com.example.book.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.R
import com.example.book_v2.data.database.ReportData
import com.example.book_v2.data.oop.SmallLetterPage
import com.example.book_v2.databinding.SmallLetterPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.interfaces.TaskCompletionListener
import com.example.book_v2.services.loadImageIntoView
import com.example.book_v2.services.setHighLightedText
import com.example.book_v2.ui.Effects.Animation
import com.example.book_v2.ui.activities.BookCoverActivity.Companion.colorArr
import com.example.book_v2.ui.customviews.DrawLetterView
import com.example.book_v2.ui.fragments.PageCompletedFragment
import com.example.book_v2.utilities.DBOperations
import com.example.book_v2.utilities.DrawLetters
import com.example.book_v2.utilities.TextToSpeechSetUp
import dev.sasikanth.colorsheet.ColorSheet

class SmallLetterFragment(
    private val listener: PageNavListeners,
    private val pageData: SmallLetterPage,
) : Fragment(), TaskCompletionListener {

    private lateinit var binding: SmallLetterPageLayoutBinding
    private lateinit var textToSpeech: TextToSpeech
    private var isPenBarOpen = false
    private var isToolBarOpen = false
    private var strokeSize = 4
    private var strokeColor = Color.BLACK
    private var completedView = 0
    private var totalAccuracy = 0.0
    private var currentPanel = -1
    private val viewsList = ArrayList<DrawLetterView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SmallLetterPageLayoutBinding.inflate(layoutInflater, container, false)
        viewsList.add(binding.letter1)
        viewsList.add(binding.letter2)
        viewsList.add(binding.letter3)
        viewsList.add(binding.letter4)
        viewsList.add(binding.letter5)
        viewsList.add(binding.letter6)
        viewsList.add(binding.letter7)
        viewsList.add(binding.letter8)
        viewsList.add(binding.letter9)

        textToSpeech = TextToSpeechSetUp.newInstanceTTS(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPage()
        setListeners()

        val smallLetterData = DrawLetters.getAllPointsFromFile(pageData.smallLetterIndexesFile)

        binding.letter1.post {
            pageData.smallLetterRepresentation = DrawLetters.ShiftPointsInScreen(
                binding.letter1.width,
                binding.letter1.height,
                smallLetterData,
            )

            for (item in viewsList) {
                item.setCircleList(pageData.smallLetterRepresentation)
                item.currentStrokeWidth = strokeSize
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

        binding.micTestPage.setOnClickListener {
            if (textToSpeech.isSpeaking) {
                textToSpeech.stop()
                textToSpeech.speak(pageData.letter, TextToSpeech.QUEUE_ADD, null, null)
            }
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
                },
            )
                .show(requireActivity().supportFragmentManager)
            strokeSize = 48
            for (item in viewsList) {
                item.currentStrokeWidth = strokeSize
                item.currentStrokeColor = strokeColor
            }
        }

        binding.letter1.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 0
            }
            return@setOnTouchListener false
        }
        binding.letter2.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 1
            }
            return@setOnTouchListener false
        }
        binding.letter3.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 2
            }
            return@setOnTouchListener false
        }
        binding.letter4.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 3
            }
            return@setOnTouchListener false
        }
        binding.letter5.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 4
            }
            return@setOnTouchListener false
        }
        binding.letter6.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 5
            }
            return@setOnTouchListener false
        }
        binding.letter7.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 6
            }
            return@setOnTouchListener false
        }
        binding.letter8.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 7
            }
            return@setOnTouchListener false
        }
        binding.letter9.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 8
            }
            return@setOnTouchListener false
        }

        binding.toolBar.undo.setOnClickListener {
            when (currentPanel) {
                0 -> {
                    binding.letter1.undoLastAction()
                }

                1 -> {
                    binding.letter2.undoLastAction()
                }

                2 -> {
                    binding.letter3.undoLastAction()
                }

                3 -> {
                    binding.letter4.undoLastAction()
                }

                4 -> {
                    binding.letter5.undoLastAction()
                }

                5 -> {
                    binding.letter6.undoLastAction()
                }

                6 -> {
                    binding.letter7.undoLastAction()
                }

                7 -> {
                    binding.letter8.undoLastAction()
                }

                8 -> {
                    binding.letter9.undoLastAction()
                }
            }
        }

        binding.toolBar.redo.setOnClickListener {
            when (currentPanel) {
                0 -> {
                    binding.letter1.redoLastAction()
                }

                1 -> {
                    binding.letter2.redoLastAction()
                }

                2 -> {
                    binding.letter3.redoLastAction()
                }

                3 -> {
                    binding.letter4.redoLastAction()
                }

                4 -> {
                    binding.letter5.redoLastAction()
                }

                5 -> {
                    binding.letter6.redoLastAction()
                }

                6 -> {
                    binding.letter7.redoLastAction()
                }

                7 -> {
                    binding.letter8.redoLastAction()
                }

                8 -> {
                    binding.letter9.redoLastAction()
                }
            }
        }

        binding.toolBar.colorPallete.setOnClickListener {
            ColorSheet().colorPicker(
                colors = colorArr,
                noColorOption = true,
                listener = { color ->
                    strokeColor = color
                    for (item in viewsList) {
                        item.currentStrokeColor = strokeColor
                    }
                },
            )
                .show(requireActivity().supportFragmentManager)
        }

        binding.toolBar.smallFont.setOnClickListener {
            strokeSize = 2
            for (item in viewsList) {
                item.currentStrokeWidth = strokeSize
            }
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.toolBar.mediumFont.setOnClickListener {
            strokeSize = 4
            for (item in viewsList) {
                item.currentStrokeWidth = strokeSize
            }
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.toolBar.largeFont.setOnClickListener {
            strokeSize = 8
            for (item in viewsList) {
                item.currentStrokeWidth = strokeSize
            }
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }
    }

    private fun setUpPage() {
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        binding.topOfPage.pageTitle.text = pageData.title
        binding.animalImage.loadImageIntoView(pageData.animalImagePath)
        binding.letterTxt.text = pageData.letter
        setHighLightedText(binding.letterTxt, pageData.letter, Color.BLACK)
        for (item in viewsList) {
            item.completionListener = this
        }
    }

    override fun onTaskCompleted(accuracy: Double) {
        completedView++
        totalAccuracy += accuracy
        if (completedView == 9) {
            totalAccuracy /= 9
            val fragment = PageCompletedFragment(totalAccuracy)
            fragment.show(parentFragmentManager, "fragment")
            val celebrate = Animation.getInstance()
            celebrate.drawable = ResourcesCompat.getDrawable(resources, R.drawable.star, null)
            // binding.celebrationView.start(celebrate.startRainAnimation())
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
                binding.letter1.handleInaccurateHandWriting(x, y)
            }

            1 -> {
                binding.letter2.handleInaccurateHandWriting(x, y)
            }

            2 -> {
                binding.letter3.handleInaccurateHandWriting(x, y)
            }

            3 -> {
                binding.letter4.handleInaccurateHandWriting(x, y)
            }

            4 -> {
                binding.letter5.handleInaccurateHandWriting(x, y)
            }

            5 -> {
                binding.letter6.handleInaccurateHandWriting(x, y)
            }

            6 -> {
                binding.letter7.handleInaccurateHandWriting(x, y)
            }

            7 -> {
                binding.letter8.handleInaccurateHandWriting(x, y)
            }

            8 -> {
                binding.letter9.handleInaccurateHandWriting(x, y)
            }
        }
    }

    override fun onProgressAchieved(progress: Double, overAll: Int) {
        binding.userProgress.max = overAll * 9
        binding.userProgress.progress = (totalAccuracy + progress).toInt()
    }
}
