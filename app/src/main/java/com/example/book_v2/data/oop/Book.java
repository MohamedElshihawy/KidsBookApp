package com.example.book_v2.data.oop;

import com.example.book_v2.data.oop.Page;

import java.util.List;

public class Book {
    private long id ;
    private String Title;
    private List<Page> bookPages;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<Page> getBookPages() {
        return bookPages;
    }

    public void setBookPages(List<Page> bookPages) {
        this.bookPages = bookPages;
    }
}
