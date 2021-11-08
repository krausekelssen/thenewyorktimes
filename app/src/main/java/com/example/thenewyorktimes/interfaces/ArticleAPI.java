package com.example.thenewyorktimes.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleAPI {

    /**
     * Este es el APIService de las noticias, se envía el API_KEY de NYT por parametro
     * y se consulta 1.json que trae las noticias mas vistas de las ultimas 24 horas
     * @param apikey
     * @return
     */
    @GET("1.json")
    Call<JsonObject> getArticle(@Query("api-key") String apikey);
}
