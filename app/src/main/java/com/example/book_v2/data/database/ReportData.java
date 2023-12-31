package com.example.book_v2.data.database;

public class ReportData {
    private int id;
    private int pageNum;
    private String letter;
    private double score;

    public ReportData(int id, int pageNum, String letter, double score) {
        this.id = id;
        this.pageNum = pageNum;
        this.letter = letter;
        this.score = score;
    }

    public ReportData(int pageNum, String letter, double score) {
        this.pageNum = pageNum;
        this.letter = letter;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
