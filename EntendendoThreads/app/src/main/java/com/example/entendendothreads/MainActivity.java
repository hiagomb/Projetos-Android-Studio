package com.example.entendendothreads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciar_thread(View view){
        MyThread thread= new MyThread();
        thread.start();
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {
            
        }
    }


    class MyThread extends Thread{
        @Override
        public void run() {
            for(int i=0; i<15; i++){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}