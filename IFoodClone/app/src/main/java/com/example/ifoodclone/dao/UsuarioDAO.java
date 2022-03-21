package com.example.ifoodclone.dao;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ifoodclone.activity.EmpresaActivity;
import com.example.ifoodclone.activity.HomeActivity;
import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UsuarioDAO {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Context context;

    public UsuarioDAO(Context context){
        this.context= context;
    }

    public boolean cadastrar(Usuario usuario){
        firebaseAuth= FirebaseSettings.getFirebaseAuth();
        databaseReference= FirebaseSettings.getDatabaseReference();

        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    databaseReference.child("usuarios").child(usuario.getId()).setValue(usuario);
                    Toast.makeText(context, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    if(usuario.getTipo().equalsIgnoreCase("empresa")){
                        context.startActivity(new Intent(context, EmpresaActivity.class));
                    }else{
                        context.startActivity(new Intent(context, HomeActivity.class));
                    }
                }else{
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException weak){
                        Toast.makeText(context, "Digite uma senha mais forte" +
                                "", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthUserCollisionException collision){
                        Toast.makeText(context, "Este email já está cadastrado" +
                                "", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthInvalidCredentialsException invalid){
                        Toast.makeText(context, "Digite um email válido" +
                                "", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return true;
    }

    public boolean logar(Usuario usuario){
        firebaseAuth= FirebaseSettings.getFirebaseAuth();
        String id= Base64Custom.encode64(usuario.getEmail());
        databaseReference= FirebaseSettings.getDatabaseReference();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                    databaseReference.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Usuario u= snapshot.getValue(Usuario.class);
                                if(u.getTipo().equalsIgnoreCase("empresa")){
                                    context.startActivity(new Intent(context, EmpresaActivity.class));
                                }else{
                                    context.startActivity(new Intent(context, HomeActivity.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(context, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

}
