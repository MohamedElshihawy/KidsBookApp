package com.example.book_v2.database;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.book_v2.R;

import java.util.ArrayList;

public class a_report_fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<scoreData> allData;
    database db;
    adapterReport adapter;

    private String mParam1;
    private String mParam2;

    public a_report_fragment() {
        // Required empty public constructor
    }

    public static a_report_fragment newInstance(String param1, String param2) {
        a_report_fragment fragment = new a_report_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.a_report_fragment, container, false);
        return v;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv=view.findViewById(R.id.rv);
//        ArrayList<scoreData>test=new ArrayList<>();
        allData=new ArrayList<>();
        database db=new database(getContext());
        allData=db.getAllData();
//        test.add(new scoreData(1,"ا",25));
//        test.add(new scoreData(2,"ب",30));
//        test.add(new scoreData(3,"م",65));
//        test.add(new scoreData(20,"ح",42));
//        test.add(new scoreData(66,"م",90));
//        test.add(new scoreData(48,"ف",80));
//        test.add(new scoreData(30,"ص",6));
//        test.add(new scoreData(2,"ش",4));
//        test.add(new scoreData(3,"ض",79));
        adapter=new adapterReport(allData);
        adapter.notifyDataSetChanged();
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rv.setAdapter(adapter);
    }
}