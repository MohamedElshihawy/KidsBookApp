package com.example.book_v2.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book.ui.fragments.PageCompletedFragment
import com.example.book_v2.R
import com.example.book_v2.data.database.ReportData
import com.example.book_v2.data.oop.MediumLetterPage
import com.example.book_v2.databinding.MediumLetterPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.services.interfaces.TaskCompletionListener
import com.example.book_v2.services.loadImageIntoView
import com.example.book_v2.ui.Effects.Animation
import com.example.book_v2.ui.activities.BookCoverActivity.Companion.colorArr
import com.example.book_v2.utilities.DBOperations
import com.example.book_v2.utilities.DrawLetters
import com.example.book_v2.utilities.TextToSpeechSetUp
import dev.sasikanth.colorsheet.ColorSheet

class MediumLetterFragment(
    private val listener: PageNavListeners,
    private val pageData: MediumLetterPage,
) : Fragment(), TaskCompletionListener {

    private lateinit var binding: MediumLetterPageLayoutBinding
    private lateinit var textToSpeech: TextToSpeech
    private var isPenBarOpen = false
    private var isToolBarOpen = false
    private var strokeSize = 10
    private var currentPanel = -1
    private var completedViews = 0
    private var totalAccuracy = 0.0
    private var strokeColor = Color.BLACK

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MediumLetterPageLayoutBinding.inflate(layoutInflater, container, false)

        textToSpeech = TextToSpeechSetUp.newInstanceTTS(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        setUpPage()

        val largeLetterData = DrawLetters.getAllPointsFromFile(pageData.mediumLetterIndexesFile)

        binding.letterView1.post {
            val final = DrawLetters.ShiftPointsInScreen(
                binding.letterView1.width,
                binding.letterView1.height,
                largeLetterData,
            )
            binding.letterView1.setCircleList(final)
            binding.letterView2.setCircleList(final)
            binding.letterView3.setCircleList(final)
            binding.letterView4.setCircleList(final)
            pageData.mediumLetterRepresentation = final
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.bottomOfPage.nextBtn.setOnClickListener { listener.nextPage() }
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
        binding.letterView3.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 2
            }
            return@setOnTouchListener false
        }
        binding.letterView4.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentPanel = 3
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
            ColorSheet().colorPicker(colors = colorArr, noColorOption = true, listener = { color ->
                strokeColor = color
                updateStrokeColor(strokeColor)
            }).show(requireActivity().supportFragmentManager)
            strokeSize = 48
            updateStrokeWidth(strokeSize)
        }

        binding.toolBar.undo.setOnClickListener {
            when (currentPanel) {
                0 -> {
                    binding.letterView1.undoLastAction()
                }

                1 -> {
                    binding.letterView2.undoLastAction()
                }

                2 -> {
                    binding.letterView3.undoLastAction()
                }

                3 -> {
                    binding.letterView4.undoLastAction()
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

                2 -> {
                    binding.letterView3.redoLastAction()
                }

                3 -> {
                    binding.letterView4.redoLastAction()
                }

                else -> {
                    Toast.makeText(requireContext(), "nothing to redo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.toolBar.colorPallete.setOnClickListener {
            ColorSheet().colorPicker(colors = colorArr, noColorOption = true, listener = { color ->
                strokeColor = color
                updateStrokeColor(strokeColor)
            }).show(requireActivity().supportFragmentManager)
        }

        binding.toolBar.smallFont.setOnClickListener {
            strokeSize = 4
            updateStrokeWidth(strokeSize)
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.mediumFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.toolBar.mediumFont.setOnClickListener {
            strokeSize = 8
            updateStrokeWidth(strokeSize)
            it.setBackgroundColor(resources.getColor(R.color.tool_bar_selected_item_bg))
            binding.toolBar.smallFont.setBackgroundColor(resources.getColor(R.color.light_grey))
            binding.toolBar.largeFont.setBackgroundColor(resources.getColor(R.color.light_grey))
        }

        binding.toolBar.largeFont.setOnClickListener {
            strokeSize = 12
            updateStrokeWidth(strokeSize)
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
        binding.letterView1.completionListener = this
        binding.letterView2.completionListener = this
        binding.letterView3.completionListener = this
        binding.letterView4.completionListener = this
    }

    private fun updateStrokeWidth(strokeSize: Int) {
        binding.letterView1.currentStrokeWidth = strokeSize
        binding.letterView2.currentStrokeWidth = strokeSize
        binding.letterView3.currentStrokeWidth = strokeSize
        binding.letterView4.currentStrokeWidth = strokeSize
    }

    private fun updateStrokeColor(strokeColor: Int) {
        binding.letterView1.currentStrokeColor = strokeColor
        binding.letterView2.currentStrokeColor = strokeColor
        binding.letterView3.currentStrokeColor = strokeColor
        binding.letterView4.currentStrokeColor = strokeColor
    }

    override fun onTaskCompleted(accuracy: Double) {
        completedViews++
        totalAccuracy = +accuracy
        if (completedViews == 4) {
            totalAccuracy /= 4
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

            2 -> {
                binding.letterView3.handleInaccurateHandWriting(x, y)
            }

            3 -> {
                binding.letterView4.handleInaccurateHandWriting(x, y)
            }
        }
    }

    override fun onProgressAchieved(progress: Double, overAll: Int) {
        binding.userProgress.max = overAll * 4
        binding.userProgress.progress = (progress + totalAccuracy).toInt()
    }
}
