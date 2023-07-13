package com.example.book_v2.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.book_v2.databinding.PageCompletionLayoutBinding
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class PageCompletedFragment(private var accuracy: Double) : DialogFragment() {

    private lateinit var binding: PageCompletionLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageCompletionLayoutBinding.inflate(layoutInflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Log.e("TAG", "onViewCreated: $accuracy")

        GlobalScope.launch(Dispatchers.Main) {

            if (accuracy in 30.0..55.0) {
                binding.star1.visibility = View.VISIBLE
                YoYo.with(Techniques.ZoomInUp)
                    .duration(1500)
                    .playOn(binding.star1)
            } else if (accuracy in 56.0..75.0) {
                binding.star1.visibility = View.VISIBLE
                YoYo.with(Techniques.ZoomInUp)
                    .duration(1000)
                    .playOn(binding.star1)

                binding.star2.visibility = View.VISIBLE
                delay(1000)
                YoYo.with(Techniques.ZoomInUp)
                    .duration(1000)
                    .playOn(binding.star2)
            } else if (accuracy > 75) {
                binding.star1.visibility = View.VISIBLE
                YoYo.with(Techniques.ZoomInUp)
                    .duration(1000)
                    .playOn(binding.star1)

                binding.star2.visibility = View.VISIBLE
                delay(1000)
                YoYo.with(Techniques.ZoomInUp)
                    .duration(1000)
                    .playOn(binding.star2)

                binding.star3.visibility = View.VISIBLE
                delay(1000)
                YoYo.with(Techniques.ZoomInUp)
                    .duration(1000)
                    .playOn(binding.star3)
            } else {
                Log.e("TAG", "onViewCreated: bad Accuracy ")
//                binding.star1.visibility = View.VISIBLE
//                YoYo.with(Techniques.ZoomInUp)
//                    .duration(1000)
//                    .playOn(binding.star1)
//                delay(1000)
//                binding.star2.visibility = View.VISIBLE
//                delay(1000)
//                YoYo.with(Techniques.ZoomInUp)
//                    .duration(1000)
//                    .playOn(binding.star2)
//                delay(1000)
//                binding.star3.visibility = View.VISIBLE
//                delay(1000)
//                YoYo.with(Techniques.ZoomInUp)
//                    .duration(1000)
//                    .playOn(binding.star3)
            }
        }
        setListeners()
    }


    private fun setListeners() {
        binding.checked.setOnClickListener {
            this.dismiss()
        }

        binding.canceled.setOnClickListener {
            this.dismiss()
        }
    }




}