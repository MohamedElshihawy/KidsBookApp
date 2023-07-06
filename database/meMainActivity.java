package com.example.book_v2.database;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.RecoverySystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_v2.R;

import java.util.ArrayList;

public class meMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_report_rv);
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        a_report_fragment reportFragment=new a_report_fragment();
        ft.replace(R.id.fragments_containertt,reportFragment);
        ft.commit();

    }
}
