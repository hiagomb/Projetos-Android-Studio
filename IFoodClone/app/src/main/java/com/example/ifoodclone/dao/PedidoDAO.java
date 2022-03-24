package com.example.ifoodclone.dao;

import com.example.ifoodclone.helper.FirebaseSettings;
import com.example.ifoodclone.model.Pedido;
import com.google.firebase.database.DatabaseReference;

public class PedidoDAO {

    private DatabaseReference databaseReference;

    public boolean salvar_pedido_usuario(Pedido pedido){
        databaseReference= FirebaseSettings.getDatabaseReference().
                child("pedidos_usuario").child(pedido.getId_empresa()).child(pedido.getId_cliente());

        if(pedido.getId_pedido() == null){
            pedido.setId_pedido(databaseReference.push().getKey());
        }

        databaseReference.setValue(pedido);

        return true;
    }
}
