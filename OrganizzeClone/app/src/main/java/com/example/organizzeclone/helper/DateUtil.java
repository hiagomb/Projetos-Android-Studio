package com.example.organizzeclone.helper;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String getDataAtual(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public static String getDataAtualForMov(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyyyy");
        return simpleDateFormat.format(date);
    }

}
