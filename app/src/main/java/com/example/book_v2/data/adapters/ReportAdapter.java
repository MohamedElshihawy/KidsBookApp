package com.example.book_v2.data.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_v2.R;
import com.example.book_v2.data.database.ReportData;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    ArrayList<ReportData> dataArray;

    public ArrayList<ReportData> getDataArray() {
        return dataArray;
    }

    public void setDataArray(ArrayList<ReportData> dataArray) {
        this.dataArray = dataArray;
    }

    public ReportAdapter(ArrayList<ReportData> dataArray) {
        this.dataArray = dataArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, null, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReportData item = dataArray.get(position);
        holder.letter.setText(item.getLetter() + "الحرف : ");
        holder.score.setText(item.getScore() + "");
        holder.pageNum.setText(item.getPageNum() + "رقم الصفحة : ");


    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView letter, score, pageNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.letterName);
            score = itemView.findViewById(R.id.score_tv);
            pageNum = itemView.findViewById(R.id.page_num);

        }
    }
}

