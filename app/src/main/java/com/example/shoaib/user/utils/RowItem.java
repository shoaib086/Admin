package com.example.shoaib.user.utils;

public class RowItem {

    private String title;


    public RowItem(String title) {

        this.title = title;

    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title;
    }
}