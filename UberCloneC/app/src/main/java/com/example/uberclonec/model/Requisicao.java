package com.example.uberclonec.model;

import java.io.Serializable;

public class Requisicao implements Serializable {

    private String id;
    private String status;
    private Boolean corrida_atual;
    private String id_passageiro;
    private String id_motorista;
    private Destino destino;
    private Double latitude_motorista;
    private Double longitude_motorista;

    public static final String STATUS_AGUARDANDO= "aguardando";
    public static final String STATUS_A_CAMINHO= "a_caminho";
    public static final String STATUS_VIAGEM= "viagem";
    public static final String STATUS_FINALIZADA= "finalizada";
    public static final String POS_CORRIDA= "pos_corrida";

    public Requisicao() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public Boolean getCorrida_atual() {
        return corrida_atual;
    }

    public void setCorrida_atual(Boolean corrida_atual) {
        this.corrida_atual = corrida_atual;
    }

    public String getId_passageiro() {
        return id_passageiro;
    }

    public void setId_passageiro(String id_passageiro) {
        this.id_passageiro = id_passageiro;
    }

    public String getId_motorista() {
        return id_motorista;
    }

    public void setId_motorista(String id_motorista) {
        this.id_motorista = id_motorista;
    }

    public Double getLatitude_motorista() {
        return latitude_motorista;
    }

    public void setLatitude_motorista(Double latitude_motorista) {
        this.latitude_motorista = latitude_motorista;
    }

    public Double getLongitude_motorista() {
        return longitude_motorista;
    }

    public void setLongitude_motorista(Double longitude_motorista) {
        this.longitude_motorista = longitude_motorista;
    }
}
