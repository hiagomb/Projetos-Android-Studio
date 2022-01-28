package com.example.whatsappclone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permission {

    public static boolean valida_Permissoes(Activity activity, String[] permissoes, int request_code){

        if(Build.VERSION.SDK_INT>= 23){
            List<String> lista_a_permitir= new ArrayList<>();

            for(String permissao: permissoes){
               boolean validaPermissao= ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
               if(!validaPermissao){//se não tem a permissao ainda, tenho que solicitar
                   lista_a_permitir.add(permissao);
               }
            }

            if(lista_a_permitir.isEmpty()){
                return true;
            }else{
                String[] lista_String= new String[lista_a_permitir.size()];
                lista_a_permitir.toArray(lista_String);
                ActivityCompat.requestPermissions(activity, lista_String, request_code);
            }
        }

        return true;
    }
}
