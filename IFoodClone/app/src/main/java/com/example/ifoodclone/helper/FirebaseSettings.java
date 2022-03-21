package com.example.ifoodclone.helper;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseSettings {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference storage;
    private static String id_user;

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

    public static StorageReference getStorage(){
        if(storage == null){
            storage= FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

    public static String getId_user(){
        if(id_user == null){
            id_user= Base64Custom.encode64(getFirebaseAuth().getCurrentUser().getEmail());
        }
        return id_user;
    }
    
}
