package com.example.ifoodclone.helper;

import android.util.Base64;

public class Base64Custom {

    public static String encode64(String value){
        return Base64.encodeToString(value.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decode64(String value){
        return new String(Base64.decode(value, Base64.DEFAULT));
    }

}
