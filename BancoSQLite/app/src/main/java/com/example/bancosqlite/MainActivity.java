package com.example.bancosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            //criando database
            SQLiteDatabase db= openOrCreateDatabase("app", MODE_PRIVATE, null);

            //criar tabela
            db.execSQL("create table if not exists Pessoas(" +
                    "nome varchar(50)," +
                    "idade int(3)" +
                    ")");

            //inserir dados
            db.execSQL("insert into Pessoas(nome, idade) values ('Maria', 24)");
            db.execSQL("insert into Pessoas(nome, idade) values ('Hiago', 23)");

            //recuperar dados
            Cursor cursor= db.rawQuery("select * from Pessoas", null);
            cursor.moveToFirst();
            while(cursor!= null){
                System.out.println("Nome: "+cursor.getString(0)+" - Idade: "+cursor.getInt(1));
                cursor.moveToNext();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}