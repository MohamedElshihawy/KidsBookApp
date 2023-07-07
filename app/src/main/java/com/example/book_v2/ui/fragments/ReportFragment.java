package com.example.book_v2.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.book_v2.data.adapters.ReportAdapter;
import com.example.book_v2.data.database.ReportData;
import com.example.book_v2.data.database.database;
import com.example.book_v2.databinding.ReportFragmentBinding;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    private ReportFragmentBinding binding;
    ArrayList<ReportData> allData;
    ReportAdapter adapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ReportFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        ArrayList<scoreData>test=new ArrayList<>();
        allData = new ArrayList<>();
        database db = new database(getContext());
        allData = db.getAllData();
//        test.add(new scoreData(1,"ا",25));
//        test.add(new scoreData(2,"ب",30));
//        test.add(new scoreData(3,"م",65));
//        test.add(new scoreData(20,"ح",42));
//        test.add(new scoreData(66,"م",90));
//        test.add(new scoreData(48,"ف",80));
//        test.add(new scoreData(30,"ص",6));
//        test.add(new scoreData(2,"ش",4));
//        test.add(new scoreData(3,"ض",79));
        adapter = new ReportAdapter(allData);
        adapter.notifyDataSetChanged();
        binding.rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rv.setAdapter(adapter);


        binding.topOfPage.pageTitle.setText("التقيم");

        binding.backButton.setOnClickListener(
                v -> {
                    requireActivity().onBackPressed();
                }
        );
    }
}