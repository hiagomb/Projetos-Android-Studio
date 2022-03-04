package com.example.organizzeclone.dao;

import androidx.annotation.NonNull;

import com.example.organizzeclone.activity.PrincipalActivity;
import com.example.organizzeclone.helper.Base64Mine;
import com.example.organizzeclone.helper.FirebaseSettings;
import com.example.organizzeclone.model.Movimentacao;
import com.example.organizzeclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MovimentacaoDAO {

    public boolean salvar_movimentacao(Movimentacao movimentacao){
        DatabaseReference databaseReference= FirebaseSettings.getDatabaseReference();
        String id_user = Base64Mine.encode(FirebaseSettings.
                getFirebaseAuth().getCurrentUser().getEmail());
        String id_mes_ano = movimentacao.getData().substring(3);
        id_mes_ano= id_mes_ano.replace("/", "");
        databaseReference.child("movimentacao").child(id_user).child(id_mes_ano).
                push().setValue(movimentacao);
        databaseReference.child("usuarios").child(id_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!= null){
                    Usuario usuario= snapshot.getValue(Usuario.class);
                    if(movimentacao.getTipo().equalsIgnoreCase("r")){
                        usuario.setReceita_total(usuario.getReceita_total() +
                               Double.parseDouble(movimentacao.getValor()));
                    }else{
                        usuario.setDespesa_total(usuario.getDespesa_total() +
                                Double.parseDouble(movimentacao.getValor()));
                    }
                    databaseReference.child("usuarios").child(id_user).setValue(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        new PrincipalActivity().recuperar_resumo();
        return true;
    }
}
