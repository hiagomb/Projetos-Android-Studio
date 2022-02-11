package com.example.whatsappclone.dao;

import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.ConfigFirebase;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Grupo;
import com.example.whatsappclone.model.Usuario;
import com.google.firebase.database.DatabaseReference;

public class GrupoDAO {

    public boolean salvar(Grupo grupo){
        try{
            DatabaseReference reference= ConfigFirebase.getFirebase().child("grupos").
                    child(grupo.getId());
            reference.setValue(grupo);

            //salvar conversas
            for(Usuario usuario: grupo.getMembros()){
                Conversa conversa= new Conversa();
                conversa.setIsGroup("true");
                conversa.setGrupo(grupo);
                conversa.setId_usuario(grupo.getId());
                conversa.setMensagem("");
                conversa.setNome(usuario.getNome());
                ConfigFirebase.getFirebase().child("conversas").
                        child(Base64Custom.encode64(usuario.getEmail())).child(grupo.getId()).setValue(conversa);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
