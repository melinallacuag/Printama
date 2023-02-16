package com.anggastudio.sample.WebApiSVEN.Controllers;

import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Terminal;
import com.anggastudio.sample.WebApiSVEN.Models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppSvenAPI {

    @GET("api/users/listado")
    Call<List<Users>> getUsers();

    @GET("api/users/listado/{id}")
    Call<List<Users>> findUsers(@Path("id") String id);

    @GET("api/company/listado")
    Call<List<Company>> getCompany();

    @GET("api/company/listado/{id}")
    Call<List<Company>> findCompany(@Path("id") Integer id);

    @GET("api/terminal/listado")
    Call<List<Terminal>> getTerminal();

    @GET("api/terminal/listado/{id}")
    Call<List<Terminal>> findTerminal(@Path("id") String id);

    @GET("api/lados/listado")
    Call<List<Lados>> getLados();

    @GET("api/lados/listado/{id}")
    Call<List<Lados>> findLados(@Path("id") String id);

}
