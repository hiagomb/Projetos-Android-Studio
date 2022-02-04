package com.example.whatsappclone.helper;

import android.util.Base64;

public class Base64Custom {

    public static String encode64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decode64(String texto_codificado){
        return new String(Base64.decode(texto_codificado, Base64.DEFAULT));
    }
}
