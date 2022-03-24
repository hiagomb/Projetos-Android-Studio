package com.example.ifoodclone.helper;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.ifoodclone.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity){
        this.activity= activity;
    }

    public void load_alert_dialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);
        LayoutInflater inflater= activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);
        alertDialog= builder.create();
        alertDialog.show();
    }

    public void dismiss_dialog(){
        alertDialog.dismiss();
    }
}
