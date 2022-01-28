package com.example.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String NOME_ARQUIVO= "whatsapp_preferencias";
    private SharedPreferences.Editor editor;

    public Preferences(Context context) {
        this.context= context;
        sharedPreferences= context.getSharedPreferences(NOME_ARQUIVO, 0);//private
        editor= sharedPreferences.edit();
    }

    public void salvar_usuario_preferences(String nome, String phone, String token){
        editor.putString("nome", nome);
        editor.putString("phone", phone);
        editor.putString("token", token);
        editor.commit();
    }

    public HashMap<String, String> getDadosUsuario(){
        HashMap<String, String> dados_usuario= new HashMap<>();
        dados_usuario.put("nome", sharedPreferences.getString("nome", "default"));
        dados_usuario.put("phone", sharedPreferences.getString("phone", "default"));
        dados_usuario.put("token", sharedPreferences.getString("token", "default"));

        return dados_usuario;
    }
}
