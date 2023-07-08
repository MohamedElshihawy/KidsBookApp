package com.example.book_v2.ui.activities // ktlint-disable package-name

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.data.viewmodels.BaseActivityViewModel
import com.example.book_v2.databinding.ActivityBookCoverBinding
import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import java.io.File
import java.util.ArrayList

class BookCoverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookCoverBinding
    private lateinit var viewModel: BaseActivityViewModel
    private lateinit var pagesData: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookCoverBinding.inflate(
            LayoutInflater.from(
                this,
            ),
        )
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }

        viewModel = ViewModelProvider(this)[BaseActivityViewModel::class.java]

        viewModel.filesMetaDataLiveData.observe(this) {
            pagesData = it as ArrayList<String>
        }

        setListeners()

        viewModel.getMetaDataOfAllPages(DATA_PATH)

        runStartAnimation()
    }

    private fun runStartAnimation() {
        MainScope().launch {
            val zoomInAnimation = YoYo.with(Techniques.ZoomIn)
                .duration(3000)
            val rollInAnimation1 = YoYo.with(Techniques.RollIn)
                .duration(1500)

            val rollInAnimation2 = YoYo.with(Techniques.RollIn)
                .duration(1500)
            val rollInAnimation3 = YoYo.with(Techniques.RollIn)
                .duration(1500)

            val fadeInAnimation = YoYo.with(Techniques.FadeIn)
                .duration(1500)

            val bounceInAnimation = YoYo.with(Techniques.Bounce)
                .duration(2500)
                .repeat(3)

            withContext(Dispatchers.Main) {
                zoomInAnimation.playOn(binding.ground)
                zoomInAnimation.playOn(binding.cloudySun)
                zoomInAnimation.playOn(binding.rightDirectionPalmeTree)
                zoomInAnimation.playOn(binding.leftDirectionPalmeTree)
                zoomInAnimation.playOn(binding.cloud2)
                zoomInAnimation.playOn(binding.cloud1)
                zoomInAnimation.playOn(binding.cloud3)
                zoomInAnimation.playOn(binding.bird)
                delay(3000)
                binding.firstLetter.visibility = View.VISIBLE
                binding.secondLetter.visibility = View.VISIBLE
                binding.thirdLetter.visibility = View.VISIBLE
                rollInAnimation1.playOn(binding.firstLetter)
                rollInAnimation2.playOn(binding.secondLetter)
                rollInAnimation3.playOn(binding.thirdLetter)

                delay(1500)
                binding.appSloganTxt.visibility = View.VISIBLE
                fadeInAnimation.playOn(binding.appSloganTxt)
                delay(1500)

                binding.startButton.visibility = View.VISIBLE
                bounceInAnimation.playOn(binding.startButton)

                binding.reportButton.visibility = View.VISIBLE
                bounceInAnimation.playOn(binding.reportButton)
            }
        }
    }

    private fun setListeners() {
        binding.startButton.setOnClickListener {
            if (pagesData.size > 0) {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .apply {
                            putStringArrayListExtra("DATA", pagesData)
                        },
                )
            }
        }

        binding.reportButton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        val DATA_PATH = (
            Environment.getExternalStorageDirectory().absolutePath +
                File.separator + "Data"
            )

        const val TAG = "MainActivity"

        var colorArr = IntArray(20)

        init {
            colorArr = intArrayOf(
                Color.BLACK,
                Color.YELLOW,
                Color.BLUE,
                Color.CYAN,
                Color.GREEN,
                Color.RED,
                Color.DKGRAY,
            )
        }
    }
}
