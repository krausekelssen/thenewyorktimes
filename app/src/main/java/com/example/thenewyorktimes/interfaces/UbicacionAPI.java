package com.example.thenewyorktimes.interfaces;

import com.example.thenewyorktimes.pojos.Ubicacion;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UbicacionAPI {
    @GET("/json")
    public Call<Ubicacion> getUbicacion();
}
