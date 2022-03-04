package com.example.requisieshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btn_recuperar;
    private TextView txt_resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_recuperar= findViewById(R.id.btn_recuperar);
        txt_resultado= findViewById(R.id.txt_resultado);

        btn_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTask myTask= new MyTask();
                String url_api= "https://blockchain.info/ticker";
                String url_api2= "https://viacep.com.br/ws/15170000/json/";
                myTask.execute(url_api, url_api2);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String url_api= strings[0];
            String url_api2= strings[1];
            StringBuffer buffer= new StringBuffer();

            try {
                URL url= new URL(url_api2);
                HttpURLConnection conexao= (HttpURLConnection) url.openConnection();

                //recupera dados em bytes
                InputStream inputStream= conexao.getInputStream();

                //decodifica para caracteres
                InputStreamReader inputStreamReader= new InputStreamReader(inputStream);

                //le os caracteres do InputStreamReader
                BufferedReader reader= new BufferedReader(inputStreamReader);
                String linha= "";
                while((linha= reader.readLine())!= null){
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //processando json
            String cidade= null;
            try {
                JSONObject jsonObject= new JSONObject(s);
                cidade= jsonObject.getString("localidade");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            txt_resultado.setText(cidade);
        }
    }
}