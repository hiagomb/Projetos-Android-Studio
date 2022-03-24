package com.example.ifoodclone.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifoodclone.R;
import com.example.ifoodclone.model.Empresa;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterEmpresas extends RecyclerView.Adapter<AdapterEmpresas.ViewVolderEmpresas> {

    private List<Empresa> lista;

    public AdapterEmpresas(List<Empresa> lista) {
        this.lista = lista;
    }

    public List<Empresa> getListaAtualizada(){
        return this.lista;
    }

    @NonNull
    @Override
    public ViewVolderEmpresas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_empresas, parent, false);
        return new ViewVolderEmpresas(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewVolderEmpresas holder, int position) {
        Empresa empresa= lista.get(position);
        holder.txt_nome.setText(empresa.getNome());
        holder.txt_categoria.setText(empresa.getCategoria());
        holder.txt_valor.setText(empresa.getValor_entrega());
        holder.txt_tempo.setText(empresa.getTempo_entrega());
        Glide.with(holder.logo.getContext()).load(
                Uri.parse(empresa.getFoto())).into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    class ViewVolderEmpresas extends  RecyclerView.ViewHolder{

        private TextView txt_nome, txt_tempo, txt_valor, txt_categoria;
        private CircleImageView logo;

        public ViewVolderEmpresas(@NonNull View itemView) {
            super(itemView);
            txt_nome= itemView.findViewById(R.id.txt_title_empresa);
            txt_categoria= itemView.findViewById(R.id.txt_categoria_empresa);
            txt_tempo= itemView.findViewById(R.id.txt_tempo_entrega_empresa);
            txt_valor= itemView.findViewById(R.id.txt_valor_entrega_empresa);
            logo= itemView.findViewById(R.id.img_empresa_logo);
        }
    }
}
