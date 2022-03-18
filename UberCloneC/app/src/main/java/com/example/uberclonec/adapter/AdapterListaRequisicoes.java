package com.example.uberclonec.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberclonec.R;
import com.example.uberclonec.helper.FirebaseSettings;
import com.example.uberclonec.helper.Local;
import com.example.uberclonec.model.Requisicao;
import com.example.uberclonec.model.Usuario;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterListaRequisicoes extends RecyclerView.Adapter<AdapterListaRequisicoes.MyViewHolder> {

    private List<Requisicao> lista;
    private Usuario motorista;
    private DatabaseReference databaseReference;

    public AdapterListaRequisicoes(List<Requisicao> lista, Usuario motorista){
        this.lista= lista;
        this.motorista= motorista;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate
                (R.layout.recycler_requisicoes, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String id_passageiro= lista.get(position).getId_passageiro();
        LatLng latLng_motorista= new LatLng(motorista.getLatitude(), motorista.getLongitude());
        databaseReference= FirebaseSettings.getDatabaseReference();
        databaseReference.child("usuarios").child(id_passageiro).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Usuario u= snapshot.getValue(Usuario.class);
                    LatLng latlng_pass= new LatLng(u.getLatitude(), u.getLongitude());
                    //
                    holder.nome_passageiro.setText(u.getNome());
                    DecimalFormat decimalFormat= new DecimalFormat("0.00");
                    String dist= decimalFormat.format(Local.calcular_valor_ou_dist(latLng_motorista ,latlng_pass,false));
                    holder.distancia.setText(dist+" km aproximadamente");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nome_passageiro, distancia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_passageiro= itemView.findViewById(R.id.text_nome_passageiro);
            distancia= itemView.findViewById(R.id.text_distancia);
        }
    }
}
