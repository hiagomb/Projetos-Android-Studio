package com.example.whatsappclone.model;

import com.google.firebase.database.Exclude;

public class Mensagem {

    private String time;
    private String message;

    public Mensagem() {
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
