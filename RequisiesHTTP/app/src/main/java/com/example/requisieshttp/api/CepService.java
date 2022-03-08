package com.example.requisieshttp.api;

import com.example.requisieshttp.model.Cep;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CepService {

    @GET("15170000/json/")
    Call<Cep> recuperarCep();

}
