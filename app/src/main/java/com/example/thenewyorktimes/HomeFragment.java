package com.example.thenewyorktimes;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.thenewyorktimes.api.InstanceRetrofit;
import com.example.thenewyorktimes.interfaces.ArticleAPI;
import com.example.thenewyorktimes.interfaces.UbicacionAPI;
import com.example.thenewyorktimes.pojos.Article;
import com.example.thenewyorktimes.pojos.Ubicacion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.thenewyorktimes.HomeFragmentDirections.*;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener{
    /**
     * Se crean las variables globales necesarias
     */
    private SearchView svSearch;
    private Button gotoFragment2;
    private String latitud;
    private String longitud;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articleList;
    private Adapter adapter;
    private View view;

    /**
     * Esta función está encargada de inicializar los componentes de interfaz gráfica,
     * arreglos y otras variables necesarias para la ejecución (solo ocurre cuando se
     * inicia el fragmento)
     */
    public void inicializador(){
        gotoFragment2 = view.findViewById(R.id.fragment_fragment1_gotofragment2);
        svSearch = view.findViewById(R.id.svSearch);
        latitud = "";
        longitud = "";
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(requireContext());
        articleList = new ArrayList<>();
    }

    /**
     * Esta función se encarga de inicializar los listeners del fragmento
     * en este caso el listener que permite redireccionar a la ubicación actual del dispositivo
     * y el listener encargado del filtro.
     */
    public void setListeners(){
        gotoFragment2.setOnClickListener(this::onClickButton);
        svSearch.setOnQueryTextListener(this);
    }

    /**
     * Esta función se encarga de actualizar y dibujar las cartas de noticias con su información
     * haciendo uso del Adapter.java, además de llamar el listener del Adapter.java, el cual
     * está encargado de escuchar las llamadas del usuario cada vez que se presiona una carta de
     * noticia con el fin de redireccionarle al enlace de la noticia
     */
    public void setupRecyclerView(){
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new Adapter(articleList, requireContext());
        adapter.setOnClickListener(this::onClickAdapter);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * Esta función recibe por parametro la vista con el fin redireccionar al usuario al MapFragment
     * la función es llamada cuando se presiona el texto "Good Morning $country, here..."
     * @param view
     */
    public void onClickButton(View view) {
        ActionHomeFragmentToMapFragment action = actionHomeFragmentToMapFragment();
        action.setLatitud(latitud);
        action.setLongitud(longitud);
        Navigation.findNavController(view).navigate(action);
        // La siguiente línea limpia el SearchView una vez que se redireccione
        svSearch.setQuery("", false);
    }

    /**
     * Esta función recibe por parametro la vista con el fin de obtener la posición de la carta que
     * fue presionada, esto permite al sistema controlar y obtener la información de la noticia
     * para posteriormente redireccionar al usuario al enlace mediante el navegador predeterminado
     * @param view
     */
    public void onClickAdapter(View view){
        int position = recyclerView.getChildAdapterPosition(view);
        Article article = articleList.get(position);
        if(!article.getUrl().equals("")){
            Uri uri = Uri.parse(article.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else{
            Toast.makeText(requireContext(), "No se pudo obtener el enlace de la noticia.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Esta función no hace nada importante
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Esta función recibe por parametro la letra que se haya ingresado al campo de filtro
     * cada vez que se ingresa un caracter en el filtro esta función es llamada con la finalidad
     * de hacer uso del Adapter.java para filtrar las noticias a partir del texto ingresado
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    /**
     * Esta función es la primera en ejecutarse al iniciar el fragmento
     * Inicializa la vista
     * Inicializa las variables
     * Inicializa los listeners
     * Obtiene la latitud y longitud del dispositivo por medio del IP
     * Obtiene las noticias mas recientes de las úitimas 24 horas
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inicializar vista
        view = inflater.inflate(R.layout.fragment_home, container, false);

        inicializador();
        setListeners();

        getUbicacion();
        getNoticias();
        return view;
    }

    /**
     * Esta función llama la instancia de Retrofit y mediante el APIService obtiene
     * la ubicación del dispositivo y el país mediante la IP
     * (también en el MapFragment usa GoogleMaps, en caso de que GoogleMaps no funcione, usa la IP)
     */
    private void getUbicacion(){
        UbicacionAPI ubicacionAPI = InstanceRetrofit.getInstanceRetrofit(InstanceRetrofit.UBICACION_URL).create(UbicacionAPI.class);
        Call<Ubicacion> call = ubicacionAPI.getUbicacion();

        call.enqueue(new Callback<Ubicacion>() {
            @Override
            public void onResponse(Call<Ubicacion> call, Response<Ubicacion> response) {
                try {
                    if(response.isSuccessful()){
                        Ubicacion ubicacion = response.body();
                        assert ubicacion != null;
                        if(!ubicacion.getCountry().equals("")){
                            gotoFragment2.setText(ubicacion.getMessageCountry());
                            latitud = String.valueOf(ubicacion.getLat());
                            longitud = String.valueOf(ubicacion.getLon());
                        }
                    }
                }catch (Exception ex){
                    Toast.makeText(requireContext(), "Algo fallo al obtener la ubicación del dispositivo por medio de la IP.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Ubicacion> call, Throwable t) {
                Toast.makeText(requireContext(), "Algo fallo al obtener la ubicación del dispositivo por medio de la IP.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Esta función llama la instancia de Retrofit y mediante el APIService obtiene
     * las noticias mas vistas de las ultimas 24 horas, las obtiene como un JSONObject y las
     * transforma en un arreglo, que posteriormente será recorrido para obtener sus valores
     * y así crear instancias del objeto Article.java para mostrar las cartas listadas
     */
    private void getNoticias(){
        ArticleAPI articleAPI = InstanceRetrofit.getInstanceRetrofit(InstanceRetrofit.NYT_URL).create(ArticleAPI.class);
        Call<JsonObject> call = articleAPI.getArticle("U0m3hvIjRmqrrH3OunYmcGN0swDJ2ahO");

        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if(response.isSuccessful()){
                        articleList = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        ArrayList results = new Gson().fromJson(jsonObject.getJSONArray("results").toString(), ArrayList.class);
                        for (int i = 0; i < results.size()-1; i++) {
                            Article article = new Article();
                            LinkedTreeMap<String, Object> resultsLinkedTreeMap = (LinkedTreeMap<String, Object>) results.get(i);
                            article.setByline(resultsLinkedTreeMap.get("byline").toString());
                            article.setTitle(resultsLinkedTreeMap.get("title").toString());
                            article.setDescription(resultsLinkedTreeMap.get("abstract").toString());
                            article.setUrl(resultsLinkedTreeMap.get("url").toString());
                            article.setPublishedAt(resultsLinkedTreeMap.get("updated").toString());

                            ArrayList medias = (ArrayList) resultsLinkedTreeMap.get("media");
                            article.setUrlToImage(""); // setear la imagen de error

                            if(!medias.isEmpty()){
                                LinkedTreeMap<String, Object> mediasLinkedTreeMap = (LinkedTreeMap<String, Object>) medias.get(0);
                                ArrayList mediametadata = (ArrayList) mediasLinkedTreeMap.get("media-metadata");
                                for (int j = 0; j < mediametadata.size()-1; j++) {
                                    LinkedTreeMap<String, Object> mediametadataLinkedTreeMap = (LinkedTreeMap<String, Object>) mediametadata.get(j);
                                    article.setUrlToImage(mediametadataLinkedTreeMap.get("url").toString());
                                }
                            }
                            articleList.add(article);
                        }
                    }
                    setupRecyclerView();
                }catch (Exception ex){
                    Toast.makeText(requireContext(), "Algo fallo al obtener las noticias más vistas de las últimas 24 horas.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(requireContext(), "Algo fallo al obtener las noticias más vistas de las últimas 24 horas.", Toast.LENGTH_LONG).show();
            }
        });
    }
}