package com.example.organizzeclone.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.organizzeclone.helper.Base64Mine;
import com.example.organizzeclone.helper.FirebaseSettings;
import com.example.organizzeclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.Base64;

public class UsuarioDAO {

    private Context context;

    public UsuarioDAO(Context context){
        this.context= context;
    }


    public boolean cadastrar(Usuario usuario){
        DatabaseReference databaseReference = FirebaseSettings.getDatabaseReference();
        FirebaseAuth firebaseAuth= FirebaseSettings.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            usuario.setId(Base64Mine.encode(usuario.getEmail()));
                            System.out.println("ID: "+usuario.getId());
                            databaseReference.child("usuarios").child(usuario.getId()).
                                    setValue(usuario);
                            Toast.makeText(context, "Cadastro realizado com sucesso",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                throw task.getException();

                            }catch(FirebaseAuthWeakPasswordException weak) {
                                Toast.makeText(context, "Digite uma senha mais forte",
                                        Toast.LENGTH_SHORT).show();
                            }catch(FirebaseAuthInvalidCredentialsException invalid) {
                                Toast.makeText(context, "Digite um email válido",
                                        Toast.LENGTH_SHORT).show();
                            }catch(FirebaseAuthUserCollisionException collision){
                                Toast.makeText(context, "Este email já está cadastrado",
                                        Toast.LENGTH_SHORT).show();
                            }catch(Exception e){
                                e.printStackTrace();
                            }


                        }
                    }
                });
        return true;
    }



}
