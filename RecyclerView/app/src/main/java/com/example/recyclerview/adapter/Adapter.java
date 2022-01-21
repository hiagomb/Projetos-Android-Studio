package com.example.recyclerview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.model.Filme;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Filme> lista_filmes;

    public Adapter(List<Filme> lista) {
        lista_filmes= lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_lista= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);

        return new MyViewHolder(item_lista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Filme filme= lista_filmes.get(position);
        holder.title.setText(filme.getTitulo());
        holder.genre.setText(filme.getGenero());
        holder.year.setText(filme.getAno());
    }

    @Override
    public int getItemCount() {

        return lista_filmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title, genre, year;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.txt_title);
            genre= itemView.findViewById(R.id.txt_genre);
            year= itemView.findViewById(R.id.txt_ano);
        }
    }


}
