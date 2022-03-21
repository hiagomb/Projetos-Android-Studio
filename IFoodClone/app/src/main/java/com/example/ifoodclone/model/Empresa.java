package com.example.ifoodclone.model;

public class Empresa {

    private String id;
    private String nome;
    private String categoria;
    private String tempo_entrega;
    private String valor_entrega;
    private String foto;

    public Empresa() {
    }

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTempo_entrega() {
        return tempo_entrega;
    }

    public void setTempo_entrega(String tempo_entrega) {
        this.tempo_entrega = tempo_entrega;
    }

    public String getValor_entrega() {
        return valor_entrega;
    }

    public void setValor_entrega(String valor_entrega) {
        this.valor_entrega = valor_entrega;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
