package com.example.uberclonec.helper;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Local {

    public static double calcular_valor_ou_dist(LatLng local_inicial, LatLng local_final, boolean valor){
        Location l_inicial= new Location("Local Inicial");
        l_inicial.setLatitude(local_inicial.latitude);
        l_inicial.setLongitude(local_inicial.longitude);

        Location l_final= new Location("Local Final");
        l_final.setLatitude(local_final.latitude);
        l_final.setLongitude(local_final.longitude);

        double dist= l_inicial.distanceTo(l_final) / 1000.0;
        if(valor== true){
            return dist*8.0;
        }else{
            return dist;
        }
    }
}
