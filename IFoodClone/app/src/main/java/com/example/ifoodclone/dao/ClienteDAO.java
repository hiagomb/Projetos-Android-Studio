package com.example.ifoodclone.dao;

import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Cliente;
import com.google.firebase.database.DatabaseReference;

public class ClienteDAO {

    private DatabaseReference databaseReference;

    public boolean set_endereco(Cliente cliente){
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("clientes").child(cliente.getId()).
                setValue(cliente);

        return true;
    }
}
