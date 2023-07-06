package com.example.book_v2.ui.predictImage

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.book_v2.R
import com.example.book_v2.data.oop.LetterPredictionPage
import com.example.book_v2.database.database
import com.example.book_v2.database.scoreData
import com.example.book_v2.databinding.LetterPredictionPageLayoutBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.ui.Effects.Animation
import com.example.book_v2.utilities.TextToSpeechSetUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.apache.commons.lang.StringEscapeUtils
import java.io.ByteArrayOutputStream
import java.io.IOException


class PredictImageFragment(
    private val listener: PageNavListeners,
    private val pageData: LetterPredictionPage
) : Fragment() {

    lateinit var bitmap: Bitmap
    private lateinit var binding: LetterPredictionPageLayoutBinding
    private val prediction = MutableLiveData<String>()
    private lateinit var tts: TextToSpeech
    private var soundPlayer: MediaPlayer? = null
    //0 or 1
    private var degree = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LetterPredictionPageLayoutBinding.inflate(layoutInflater)
        tts = TextToSpeechSetUp.newInstanceTTS(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setListener()
        setUpPage()

        prediction.observe(viewLifecycleOwner) {

            val celebrate = Animation.getInstance()
            celebrate.drawable = ResourcesCompat.getDrawable(resources, R.drawable.star, null)
            if (pageData.letter.toCharArray()[0] == it.toCharArray()[1]) {
                Log.e("TAG", "onViewCreated: success")
                binding.celebrate.start(celebrate.startParadeAnimation())
                soundPlayer = MediaPlayer.create(requireContext(), R.raw.right_answer_sound)
                degree=1
                writeReport(degree);

            } else {
                soundPlayer = MediaPlayer.create(requireContext(), R.raw.wrong_answer_sound)
                binding.writeLetter.wrongLetter()
                degree=0
                writeReport(degree);
            }
            soundPlayer?.start()
        }


    }
    private fun writeReport(t: Int) {
        val data = scoreData(pageData.pageNum.toInt(),pageData.letter,
            t.toDouble()
        );
        val db= database(getContext());
        val res=db.insert(data)
        if(res){
            Log.e("TAG", "insert  : ")}
        else {
            Log.e("TAG", "update : ")
            db.updateItem(data)
        }
    }

    private fun setListener() {
        binding.speakingMicImage.setOnClickListener {
            bitmap = binding.writeLetter.getLetterImage()
            predictImage()
        }

        binding.bottomOfPage.nextBtn.setOnClickListener {
            listener.nextPage()
        }
        binding.bottomOfPage.previousBtn.setOnClickListener {
            listener.previousPage()
        }

    }

    private fun predictImage() {
        MainScope().launch(Dispatchers.IO) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            // Create request body
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "image",
                    "image.png",
                    RequestBody.create("image/*".toMediaTypeOrNull(), byteArray)
                )
                .build()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://192.168.1.8:5000/upload")
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val letter =
                            StringEscapeUtils.unescapeJava(response.body?.string()?.substring(9))
                        println(letter)
                        prediction.postValue(letter)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("TAG", "onFailure: ${e.message}")
                }
            })
        }
    }


    private fun setUpPage() {
        binding.topOfPage.pageTitle.text = pageData.title
        binding.bottomOfPage.pageNum.text = pageData.pageNum.toString()
        tts.speak(pageData.textToVoice, TextToSpeech.QUEUE_ADD, null, null)
    }


}