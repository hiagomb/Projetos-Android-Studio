package com.example.ifoodclone.helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;

import com.example.ifoodclone.R;

public class QuantidadeDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    private ImageButton btn_up, btn_down;
    private EditText edit_quant;

    public QuantidadeDialog(Activity activity){
        this.activity= activity;
        btn_up= this.activity.findViewById(R.id.btn_up);
        btn_down= this.activity.findViewById(R.id.btn_down);
        edit_quant = this.activity.findViewById(R.id.edit_quantidade);

    }

    public void load_alert_dialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);
        LayoutInflater inflater= activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.quant_dialog, null));
        builder.setCancelable(true);
        alertDialog= builder.create();
        alertDialog.show();
    }

    public void dismiss_dialog(){
        alertDialog.dismiss();
    }

    public EditText getEdit_quant() {
        return edit_quant;
    }
}
