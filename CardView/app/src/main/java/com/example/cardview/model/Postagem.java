package com.example.cardview.model;

import android.widget.ImageView;

public class Postagem {

    private String author;
    private String time;
    private int img;
    private String post_text;

    public Postagem() {
    }

    public Postagem(String author, String time, int img, String post_text) {
        this.author = author;
        this.time = time;
        this.img = img;
        this.post_text = post_text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }
}
