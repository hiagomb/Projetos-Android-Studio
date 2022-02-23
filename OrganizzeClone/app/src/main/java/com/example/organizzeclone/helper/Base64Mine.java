package com.example.organizzeclone.helper;

import android.os.Build;
import android.util.Base64;


public class Base64Mine {

    public static String encode(String string){
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decode(String string){
        return new String(Base64.decode(string, Base64.DEFAULT));
    }
}
