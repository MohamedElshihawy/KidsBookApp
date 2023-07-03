package com.example.book_v2.ui.signInUpScreen

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.book_v2.R
import com.example.book_v2.data.models.CreateUser
import com.example.book_v2.data.viewmodels.MyViewModelFactory
import com.example.book_v2.databinding.SignUpLayoutBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpLayoutBinding
    private lateinit var viewmodel: viewModel
    private val user: CreateUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpLayoutBinding.inflate(layoutInflater)

        viewmodel = ViewModelProvider(this, MyViewModelFactory())[viewmodel::class.java]

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setListener()

    }

    private fun setListener() {
        binding.oldUserTxt.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.sign_in_up_fragment_container, SignInFragment()).commit()
        }

        binding.signUpBtn.setOnClickListener {
            if (signUp()) {


            }
        }
    }

    private fun signUp(): Boolean {

        if (binding.userFirstNameEdt.text.isNullOrEmpty()) {
            binding.userFirstNameEdt.error = "من فضلك ادخل اسما صحيحا"
            return false
        } else {

        }
        if (binding.userLastNameEdt.text.isNullOrEmpty()) {
            binding.userLastNameEdt.error = "من فضلك ادخل اسما صحيحا"
            return false
        } else {

        }
        if (binding.passwordEdt.text.isNullOrEmpty() || binding.passwordEdt.text!!.length < 12) {
            binding.passwordEdt.error = "من فضلك ادخل كلمة سر صحيحة"
            return false
        } else {

        }
        if (binding.confirmPasswordEdt.text.isNullOrEmpty() ||
            binding.passwordEdt.text!! == binding.confirmPasswordEdt.text
        ) {
            binding.userFirstNameEdt.error = "من فضلك ادخل اسما صحيحا"
            return false
        } else {

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.userFirstNameEdt.text.toString()).find()) {
            binding.userFirstNameEdt.error = "من فضلك ادخل بريدا صحيحا"
            return false
        } else {

        }
        return true
    }


}