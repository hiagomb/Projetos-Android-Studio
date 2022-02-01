package com.example.whatsappclone.dao;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsappclone.activity.CadastroEmailSenhaActivity;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Executor;

public class UsuarioDAO implements IUsuarioDAO{

    private FirebaseAuth auth= ConfigFirebase.getFirebaseAuth();
    private DatabaseReference reference= ConfigFirebase.getFirebase();

    @Override
    public boolean cadastrar(Usuario usuario) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(),
                    usuario.getSenha()).addOnCompleteListener(new CadastroEmailSenhaActivity().getActivity(),
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                System.out.println("deu certo");
                                usuario.setId(task.getResult().getUser().getUid());
                                salvar_usuario(usuario);
                            }else{
                                System.out.println("deu errado");
                            }
                        }
                    });
        return true;
    }

    @Override
    public boolean salvar_usuario(Usuario usuario) {
        reference.child("usuarios").child(usuario.getId()).setValue(usuario);

        return true;
    }
}
