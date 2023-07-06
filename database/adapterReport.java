package com.example.book_v2.database;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_v2.R;

import java.util.ArrayList;

public class adapterReport extends RecyclerView.Adapter<adapterReport.MyViewHolder> {
    ArrayList<scoreData> dataArray;

    public ArrayList<scoreData> getDataArray() {
        return dataArray;
    }

    public void setDataArray(ArrayList<scoreData> dataArray) {
        this.dataArray = dataArray;
    }

    public adapterReport(ArrayList<scoreData> dataArray) {
        this.dataArray = dataArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v= LayoutInflater.from(parent.getContext()).inflate(R.layout.a_report,null,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        scoreData item= dataArray.get(position);
        holder.letter.setText(item.getLetter());
        holder.score.setText(item.getScore()+"");
        holder.pageNum.setText(""+item.getPageNum());


    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView letter,letterSize,score,pageNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.letterName);
//            letterSize = itemView.findViewById(R.id.letterSizeData);
            score = itemView.findViewById(R.id.score_tv);
            pageNum = itemView.findViewById(R.id.pageNumData);

        }
    }
}

