package com.example.ifoodclone.dao;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ifoodclone.helper.Base64Custom;
import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EmpresaDAO {

    private DatabaseReference databaseReference;
    private Context context;

    public EmpresaDAO(Context context) {
        this.context= context;
    }

    public boolean salvar_dados_empresa(Empresa empresa){
        FirebaseUser user= FirebaseSettings.getFirebaseAuth().getCurrentUser();
        UserProfileChangeRequest changeRequest= new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(empresa.getFoto())).build();
        user.updateProfile(changeRequest);

        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("empresas").child(empresa.getId()).setValue(empresa);
        Toast.makeText(context, "Configurações salvas com sucesso",
                Toast.LENGTH_SHORT).show();

        return true;
    }
}
