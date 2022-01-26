package com.example.listadetarefas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetarefas.R;
import com.example.listadetarefas.model.Tarefa;

import java.util.List;

public class AdapterListaTarefas extends RecyclerView.Adapter<AdapterListaTarefas.MyViewHolder>{

    private List<Tarefa> tarefas;

    public AdapterListaTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_tarefas, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tarefa tarefa= tarefas.get(position);
        holder.txt_tarefa.setText(tarefa.getTarefa_item());
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_tarefa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tarefa= itemView.findViewById(R.id.txt_tarefa);
        }
    }
}
