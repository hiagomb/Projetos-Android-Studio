package com.example.uberclonec.dao;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.uberclonec.activity.Pass_Activity;
import com.example.uberclonec.activity.RequisicaoAcitivity;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.model.Usuario;
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

    private DatabaseReference reference_usuario;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public UsuarioDAO(Context context){
        this.context= context;
    }

    public boolean cadastrarUsuario(Usuario usuario){
        firebaseAuth= FirebaseSettings.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference_usuario= FirebaseSettings.getDatabaseReference();
                    reference_usuario.child("usuarios").child(usuario.getId()).setValue(usuario);
                    //atualiza profile
                    FirebaseUser user = FirebaseSettings.getFirebaseAuth().getCurrentUser();
                    UserProfileChangeRequest profile= new UserProfileChangeRequest.Builder()
                            .setDisplayName(usuario.getNome()).build();
                    user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                System.out.println("erro");
                            }
                        }
                    });

                    Toast.makeText(context, "Cadastrado com sucesso",
                            Toast.LENGTH_SHORT).show();
                    if(usuario.getTipo().equalsIgnoreCase("passageiro")){
                        context.startActivity(new Intent(context, Pass_Activity.class));
                    }else{
                        context.startActivity(new Intent(context, RequisicaoAcitivity.class));
                    }
                }else{
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException weakPasswordException){
                        Toast.makeText(context, "Digite uma senha mais forte",
                                Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthInvalidCredentialsException invalid){
                        Toast.makeText(context, "Digite um email válido",
                                Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthUserCollisionException user){
                        Toast.makeText(context, "Este email já está cadastrado",
                                Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return true;
    }

    public boolean logar_usuario(Usuario usuario){
        firebaseAuth= FirebaseSettings.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            reference_usuario= FirebaseSettings.getDatabaseReference();
                            reference_usuario.child("usuarios").child(usuario.getId()).
                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Usuario u= snapshot.getValue(Usuario.class);
                                                if(u.getTipo().equalsIgnoreCase("passageiro")){
                                                    context.startActivity(new Intent(context, Pass_Activity.class));
                                                }else{
                                                    context.startActivity(new Intent(context, RequisicaoAcitivity.class));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else{
                            Toast.makeText(context, "Email ou senha incorreto",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return true;
    }
}
