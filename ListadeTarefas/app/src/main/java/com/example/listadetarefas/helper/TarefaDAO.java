package com.example.listadetarefas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements ITarefaDAO{

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public TarefaDAO(Context context) {
        DbHelper db= new DbHelper(context);
        escreve= db.getWritableDatabase();
        le= db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {
        try{
            ContentValues contentValues= new ContentValues();
            contentValues.put("tarefa_item", tarefa.getTarefa_item());
            escreve.insert("Tarefas", null, contentValues);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        return false;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {
        return false;
    }

    @Override
    public List<Tarefa> listar() {
        List<Tarefa> tarefas= new ArrayList<>();
        String sql= "select * from Tarefas;";
        Cursor cursor= le.rawQuery(sql, null);

        while(cursor.moveToNext()){
            Tarefa tarefa= new Tarefa();
            tarefa.setId(cursor.getInt(cursor.getColumnIndex("id")));
            tarefa.setTarefa_item(cursor.getString(cursor.getColumnIndex("tarefa_item")));
            tarefas.add(tarefa);
        }

        return tarefas;
    }
}
