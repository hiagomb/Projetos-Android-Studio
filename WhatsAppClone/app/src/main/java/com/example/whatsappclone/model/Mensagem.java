package com.example.whatsappclone.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class Mensagem {

    private String id_rem;
    private String message;
    private String foto;

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


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
