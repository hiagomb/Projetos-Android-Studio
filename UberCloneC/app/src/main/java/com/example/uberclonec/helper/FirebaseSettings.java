package com.example.uberclonec.helper;

import com.example.uberclonec.model.Usuario;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSettings {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            databaseReference= FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            firebaseAuth= FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static Usuario getUsuarioPassageiro(Double latitude, Double longitude){
        Usuario u= new Usuario();
        u.setEmail(FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail());
        u.setId(Base64Custom.encode64(u.getEmail()));
        u.setNome(FirebaseSettings.getFirebaseAuth().getCurrentUser().getDisplayName());
        u.setTipo("passageiro");
        u.setLatitude(latitude);
        u.setLongitude(longitude);
        return u;
    }

    public static void atualiza_geofire(Double latitude, Double longitude){
        DatabaseReference databaseReference= getDatabaseReference().child("local_usuario");
        GeoFire geoFire= new GeoFire(databaseReference);
        String id_user= Base64Custom.encode64(getFirebaseAuth().getCurrentUser().getEmail());
        geoFire.setLocation(id_user, new GeoLocation(latitude, longitude));
    }

}
