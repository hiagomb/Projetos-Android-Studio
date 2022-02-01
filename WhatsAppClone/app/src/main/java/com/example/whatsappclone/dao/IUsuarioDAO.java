package com.example.whatsappclone.dao;

import com.example.whatsappclone.model.Usuario;

public interface IUsuarioDAO {

    public boolean cadastrar(Usuario usuario);

    public boolean salvar_usuario(Usuario usuario);
}
