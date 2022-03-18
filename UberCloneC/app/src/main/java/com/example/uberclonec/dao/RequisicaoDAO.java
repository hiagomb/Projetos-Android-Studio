package com.example.uberclonec.dao;

import com.example.uberclonec.helper.Base64Custom;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.model.Destino;
import com.example.uberclonec.model.Requisicao;
import com.example.uberclonec.model.Usuario;
import com.google.firebase.database.DatabaseReference;

public class RequisicaoDAO {

    private DatabaseReference databaseReference;

    public String criar_requisicao_corrida(Destino destino, Usuario u){
        Requisicao requisicao= new Requisicao();
        requisicao.setStatus(Requisicao.STATUS_AGUARDANDO);
        requisicao.setDestino(destino);
        requisicao.setCorrida_atual(true);
        String passageiro_id= Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail());
        requisicao.setId_passageiro(passageiro_id);

        databaseReference= FirebaseSettings.getDatabaseReference().child("requisicoes").child("passageiros")
             .child(passageiro_id);
        requisicao.setId(databaseReference.push().getKey());
        databaseReference.child(requisicao.getId()).setValue(requisicao);

        //atualizando a localização do usuário
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("usuarios").child(u.getId()).setValue(u);

        //salvando o id da requisição em um nó de requisições ativas
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes_ativas").child(requisicao.getId()).setValue(requisicao);

        return requisicao.getId();
    }

    public boolean cancelar_corrida(String chave_requisicao){
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes").child("passageiros").
                child(Base64Custom.encode64(FirebaseSettings.getFirebaseAuth().getCurrentUser().getEmail())).
                child(chave_requisicao).removeValue();
        databaseReference.child("requisicoes_ativas").child(chave_requisicao).removeValue();

        return true;
    }

    public boolean atualiza_status_requisicao(Requisicao requisicao){
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes_ativas").child(requisicao.getId()).setValue(requisicao);
        databaseReference.child("requisicoes").child("passageiros").
                child(requisicao.getId_passageiro()).child(requisicao.getId()).setValue(requisicao);

        return true;
    }

    public boolean exclui_requisicao_ativa(String chave_requisicao){
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes_ativas").child(chave_requisicao).removeValue();
        return true;
    }

    public boolean atualiza_local_motorista_req(Requisicao requisicao){
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("requisicoes_ativas").child(requisicao.getId()).setValue(requisicao);
        return true;
    }


}
