package com.example.whatsappclone.dao;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsappclone.activity.CadastroEmailSenhaActivity;
import com.example.whatsappclone.activity.LoginEmailSenhaActivity;
import com.example.whatsappclone.activity.MainActivity;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Executor;

public class UsuarioDAO implements IUsuarioDAO{

    private FirebaseAuth auth= ConfigFirebase.getFirebaseAuth();
    private DatabaseReference reference= ConfigFirebase.getFirebase();
    private String exception;
    private boolean result_operation;
    private Context context;

    public UsuarioDAO(Context context) {
        exception= new String();
        this.context= context;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public boolean cadastrar(Usuario usuario) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(),
                    usuario.getSenha()).addOnCompleteListener(new CadastroEmailSenhaActivity().getActivity(),
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                usuario.setId(Base64Custom.encode64(usuario.getEmail()));
                                salvar_usuario(usuario);
                                result_operation= true;
                                Toast.makeText(context, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(context, LoginEmailSenhaActivity.class));
                            }else{
                                try{
                                    throw task.getException();
                                }catch(FirebaseAuthWeakPasswordException weakPasswordException){
                                    Toast.makeText(context, "Digite uma senha mais forte", Toast.LENGTH_LONG).show();
                                }catch(FirebaseAuthInvalidCredentialsException invalid){
                                    Toast.makeText(context, "Digite um email válido", Toast.LENGTH_LONG).show();
                                }catch(FirebaseAuthUserCollisionException same){
                                    Toast.makeText(context, "Este email já está cadastrado", Toast.LENGTH_LONG).show();
                                }catch(Exception e){
                                    Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_LONG).show();
                                }
                                result_operation= false;
                            }
                        }

                    });
        return result_operation;
    }

    @Override
    public boolean salvar_usuario(Usuario usuario) {
        reference.child("usuarios").child(usuario.getId()).setValue(usuario);

        return true;
    }

    public boolean logar(Usuario usuario){
        auth.signInWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new LoginEmailSenhaActivity().getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    context.startActivity(new Intent(context, MainActivity.class));
                    Toast.makeText(context, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "Email ou senha invalidos", Toast.LENGTH_LONG).show();
                }
            }
        });

        return true;
    }

    public void checaUsuariologado(){
        if(auth.getCurrentUser()!= null){
            context.startActivity(new Intent(context, MainActivity.class));
            Toast.makeText(context, "Bem vindo(a) "+auth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean deslogar(){
        if(auth.getCurrentUser()!= null){
            auth.signOut();
        }
        return true;
    }
}
