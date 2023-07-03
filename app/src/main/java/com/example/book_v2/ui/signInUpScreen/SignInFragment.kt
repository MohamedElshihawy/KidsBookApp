package com.example.book_v2.ui.signInUpScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.book_v2.R
import com.example.book_v2.databinding.SignInLayoutBinding

class SignInFragment : Fragment() {

    private lateinit var binding: SignInLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignInLayoutBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListener()
    }


    private fun setListener() {
        binding.signInBtn.setOnClickListener {
            collectSignInData()
        }

        binding.newUserTxt.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.sign_in_up_fragment_container, SignUpFragment()).commit()
        }
    }



    private fun collectSignInData() {
        if (binding.userNameEdt.text.isNullOrEmpty() || binding.userNameEdt.text!!.length < 12) {
            binding.userNameEdt.error = " من فضلك ادخل اسم مستخدم صحيح"
        } else {
            //get username
        }

        if (binding.paswwordEdt.text.isNullOrEmpty() || binding.userNameEdt.text!!.length < 10) {
            binding.userNameEdt.error = "من فضلك ادخل كلمة سر صحيحة"
        } else {
            // get pass
        }


    }
}