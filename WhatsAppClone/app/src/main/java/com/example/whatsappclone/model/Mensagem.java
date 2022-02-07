package com.example.whatsappclone.model;

import com.google.firebase.database.Exclude;

public class Mensagem {

    private String id_rem;
    private String message;

    public Mensagem() {
    }


    public String getId_rem() {
        return id_rem;
    }

    public void setId_rem(String id_rem) {
        this.id_rem = id_rem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
