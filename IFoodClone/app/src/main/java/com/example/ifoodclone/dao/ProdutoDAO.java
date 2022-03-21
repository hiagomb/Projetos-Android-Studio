package com.example.ifoodclone.dao;

import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Produto;
import com.google.firebase.database.DatabaseReference;

public class ProdutoDAO {

    private DatabaseReference databaseReference;

    public boolean cadastrar_produto(Produto produto){
        String id= FirebaseSettings.getId_user();
        databaseReference= FirebaseSettings.getDatabaseReference().child("produtos").child(id);
        produto.setId(databaseReference.push().getKey());
        databaseReference.child(produto.getId()).setValue(produto);
        return true;
    }

    public boolean editar_produto(Produto produto){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String id= FirebaseSettings.getId_user();
        databaseReference.child("produtos").child(id).child(produto.getId()).setValue(produto);

        return true;
    }

    public boolean excluir_produto(Produto produto){
        databaseReference= FirebaseSettings.getDatabaseReference();
        String id= FirebaseSettings.getId_user();
        databaseReference.child("produtos").child(id).child(produto.getId()).removeValue();

        return true;
    }
}
