package com.example.whatsappclone.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Usuario;

import java.util.List;

public class AdapterMembrosSelecionados extends RecyclerView.Adapter<AdapterMembrosSelecionados.MyViewHolderMembros>{

    private List<Usuario> lista;

    public AdapterMembrosSelecionados(List<Usuario> lista){

        this.lista= lista;
    }

    @NonNull
    @Override
    public MyViewHolderMembros onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_selecionados, parent, false);
        return new MyViewHolderMembros(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderMembros holder, int position) {
        if(lista.get(position).getPhoto()!= null && !lista.get(position).getPhoto().isEmpty()){
            Uri uri= Uri.parse(lista.get(position).getPhoto());
            Glide.with(holder.imagem_membro.getContext()).load(uri).into(holder.imagem_membro);
        }
        holder.nome_membro.setText(lista.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolderMembros extends RecyclerView.ViewHolder{

        private TextView nome_membro;
        private ImageView imagem_membro;

        public MyViewHolderMembros(@NonNull View itemView) {
            super(itemView);
            nome_membro= itemView.findViewById(R.id.membro_selecionado_nome);
            imagem_membro= itemView.findViewById(R.id.membro_selecionado_foto);
        }
    }





}
