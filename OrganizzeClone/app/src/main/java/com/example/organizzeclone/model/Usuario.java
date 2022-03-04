package com.example.organizzeclone.model;

import com.google.firebase.database.Exclude;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private Double despesa_total;
    private Double receita_total;

    public Usuario() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getDespesa_total() {
        return despesa_total;
    }

    public void setDespesa_total(Double despesa_total) {
        this.despesa_total = despesa_total;
    }

    public Double getReceita_total() {
        return receita_total;
    }

    public void setReceita_total(Double receita_total) {
        this.receita_total = receita_total;
    }
}
