package com.example.book_v2.ui.signInUpScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.book_v2.R

class SignInUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_up_activivty)

        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_in_up_fragment_container, SignUpFragment())
            .commit()




    }
}