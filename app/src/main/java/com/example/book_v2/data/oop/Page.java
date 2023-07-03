package com.example.book_v2.data.oop;

import com.example.book_v2.data.oop.PageTypes;

import java.io.Serializable;

public abstract class Page extends Book implements Serializable {

    private PageTypes PageType;
    private String Title;
    private String timeSpent;
    private float score;
    private short pageNum;

    public Page(){

    }

    public PageTypes getPageType() {
        return PageType;
    }

    public void setPageType(PageTypes pageType) {
        PageType = pageType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public short getPageNum() {
        return pageNum;
    }

    public void setPageNum(short pageNum) {
        this.pageNum = pageNum;
    }
}
