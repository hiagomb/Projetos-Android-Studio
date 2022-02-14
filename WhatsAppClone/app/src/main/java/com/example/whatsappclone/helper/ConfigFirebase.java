package com.example.whatsappclone.helper;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public final class ConfigFirebase {

    private static DatabaseReference reference;
    private static FirebaseAuth auth;
    private static StorageReference storage;

    public static DatabaseReference getFirebase(){
        if(reference == null){
            reference= FirebaseDatabase.getInstance().getReference();
        }
        return reference;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(auth == null){
            auth= FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static StorageReference getFirebaseStorage(){
        if(storage== null){
            storage= FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

    public static void atualizarObjetoeBaseComFoto(String id, Uri uri){
        DatabaseReference reference= getFirebase().child("usuarios").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                u.setPhoto(uri.toString());
                reference.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            System.out.println("erro aqui");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static Usuario getUsuarioLogado(){
        Usuario usuario= new Usuario();
        FirebaseUser firebaseUser= getFirebaseAuth().getCurrentUser();
        usuario.setId(Base64Custom.encode64(firebaseUser.getEmail()));
        usuario.setEmail(firebaseUser.getEmail());
        if(firebaseUser.getPhotoUrl()!= null){
            usuario.setPhoto(firebaseUser.getPhotoUrl().toString());
        }
        usuario.setNome(firebaseUser.getDisplayName());


        return usuario;
    }
}
