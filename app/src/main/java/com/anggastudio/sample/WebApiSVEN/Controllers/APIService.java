package com.anggastudio.sample.WebApiSVEN.Controllers;

import com.anggastudio.sample.Adapter.Grias;
import com.anggastudio.sample.WebApiSVEN.Models.Card;
import com.anggastudio.sample.WebApiSVEN.Models.Cliente;
import com.anggastudio.sample.WebApiSVEN.Models.Company;
import com.anggastudio.sample.WebApiSVEN.Models.DetalleVenta;
import com.anggastudio.sample.WebApiSVEN.Models.Lados;
import com.anggastudio.sample.WebApiSVEN.Models.Optran;
import com.anggastudio.sample.WebApiSVEN.Models.Picos;
import com.anggastudio.sample.WebApiSVEN.Models.Placa;
import com.anggastudio.sample.WebApiSVEN.Models.Setting;
import com.anggastudio.sample.WebApiSVEN.Models.SettingTask;
import com.anggastudio.sample.WebApiSVEN.Models.Terminal;
import com.anggastudio.sample.WebApiSVEN.Models.Tipotarjeta;
import com.anggastudio.sample.WebApiSVEN.Models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    //User
    @GET("api/users/listado")
    Call<List<Users>> getUsers();

    @GET("api/users/listado/{id}")
    Call<List<Users>> findUsers(@Path("id") String id);

    //Company
    @GET("api/company/listado")
    Call<List<Company>> getCompany();

    @GET("api/company/listado/{id}")
    Call<List<Company>> findCompany(@Path("id") Integer id);

    //Terminal
    @GET("api/terminal/listado")
    Call<List<Terminal>> getTerminal();

    @GET("api/terminal/listado/{id}")
    Call<List<Terminal>> findTerminal(@Path("id") String id);

    //Cara
    @GET("api/lados/listado")
    Call<List<Lados>> getLados();

    @GET("api/lados/listado/{id}")
    Call<List<Lados>> findLados(@Path("id") String id);

    //Manguera
    @GET("api/picos/listado")
    Call<List<Picos>> getPico();

    @GET("api/picos/listado/{id}")
    Call<List<Picos>> findPico(@Path("id") String id);

    @POST("api/picos/guardar")
    Call<Picos> postPicos(@Body Picos picos);

    //Optran
    @GET("api/optran/listado/{id}")
    Call<List<Optran>> findOptran(@Path("id") String id);

    @GET("api/detalleventa/listado/{id}")
    Call<List<DetalleVenta>> findDetalleVenta(@Path("id") String id);

    // Detalle de Venta
    @GET("api/card/listado")
    Call<List<Card>> getCard();

    @GET("api/card/listado/{id}")
    Call<List<Card>> findCard(@Path("id") Integer id);

    //Cliente ruc-dni
    @GET("api/cliente/listado")
    Call<List<Cliente>> getCliente();

    @GET("api/cliente/listado/{id}")
    Call<List<Cliente>> findCliente(@Path("id") String id);

    @GET("api/cliente/listado/RUC/{id}")
    Call<List<Cliente>> findClienteRUC(@Path("id") String id);

    @GET("api/cliente/listado/DNI/{id}")
    Call<List<Cliente>> findClienteDNI(@Path("id") String id);

    //Placa
    @GET("api/placa/listado")
    Call<List<Placa>> getPlaca();

    @GET("api/placa/listado/{id}")
    Call<List<Placa>> findPlaca(@Path("id") String id);

    //Configuración
    @GET("api/setting/listado")
    Call<List<Setting>> getSetting();

    @GET("api/setting/listado/{id}")
    Call<List<Setting>> findSetting(@Path("id") String id);

    //Ajustes
    @GET("api/settingTask/listado")
    Call<List<SettingTask>> getSettingTask();

    @GET("api/settingTask/listado/{id}")
    Call<List<SettingTask>> findSettingTask(@Path("id") String id);

}
