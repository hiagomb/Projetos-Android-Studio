package com.example.calculadoradegorjeta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar seek;
    private EditText val;
    private TextView txt_seek, txt_gorjeta, txt_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seek= findViewById(R.id.seekBar);
        val= findViewById(R.id.edit_valor);
        txt_seek= findViewById(R.id.txt_seek);
        txt_gorjeta= findViewById(R.id.txt_gorj);
        txt_total= findViewById(R.id.txt_total);
        calcula_gorjeta_total();
    }


    public void calcula_gorjeta_total(){

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txt_seek.setText(i+"%");
                String value= val.getText().toString();
                if(value== null || value.equals("")){
                    txt_gorjeta.setText("R$0.00");
                    txt_total.setText("R$0.00");
                    Toast.makeText(getApplicationContext(), "Digite um valor", Toast.LENGTH_SHORT).show();
                }else{
                    double valor= Double.parseDouble(value);
                    double gorjeta= valor*i/100.0;
                    txt_gorjeta.setText("R$"+gorjeta);
                    txt_total.setText("R$"+(valor+gorjeta));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



}