package com.example.whatsappclone.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

public class Mensagem {

    private String id_rem;
    private String id_dest;
    private String message;
    private String foto;
    private String is_group;
    private String nome_usuario_for_group;

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

    public String getId_dest() {
        return id_dest;
    }

    public void setId_dest(String id_dest) {
        this.id_dest = id_dest;
    }

    public String getIs_group() {
        return is_group;
    }

    public void setIs_group(String is_group) {
        this.is_group = is_group;
    }

    public String getNome_usuario_for_group() {
        return nome_usuario_for_group;
    }

    public void setNome_usuario_for_group(String nome_usuario_for_group) {
        this.nome_usuario_for_group = nome_usuario_for_group;
    }
}
