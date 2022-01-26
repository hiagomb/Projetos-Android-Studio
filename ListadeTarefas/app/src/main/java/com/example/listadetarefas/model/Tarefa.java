package com.example.listadetarefas.model;

import java.io.Serializable;

public class Tarefa implements Serializable {

    private int id;
    private String tarefa_item;

    public Tarefa(){

    }

    public Tarefa(String tarefa_item) {
        this.tarefa_item = tarefa_item;
    }

    public String getTarefa_item() {
        return tarefa_item;
    }

    public void setTarefa_item(String tarefa_item) {
        this.tarefa_item = tarefa_item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
