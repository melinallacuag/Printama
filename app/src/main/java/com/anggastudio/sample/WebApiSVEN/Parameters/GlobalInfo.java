package com.anggastudio.sample.WebApiSVEN.Parameters;

import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;

public class GlobalInfo {


    //Company
    public static String getNameCompany10;
    public static String getBranchCompany10;
    public static String getSloganCompany10;

    //User
    public static String getName10;

    //Cara y Manguera
    public static String getCara10;
    public static String getPistola10;

    //Terminal
    public static String getterminalID10 = "";
    public static String getImei10;
    public static String getfecha10;
    public static Integer getturno10;
    public static Integer getCompanyID10;
    public static Integer getalmacenID10;
    public static Boolean getconsultaSunat10;
    public static Boolean getventaAutomatica10;
    public static Boolean getventaPlaya10;
    public static Boolean getventaTienda10;
    public static Boolean getventaCredito10;
    public static Boolean getventaTarjeta10;
    public static Boolean getventaGratuita10;
    public static Boolean getventaSerafin10;

    //Placa
    public static String getNroPlaca10;
    public static String getClienteIDPlaca10;
    public static String getClienteRZPlaca10;
    public static String getClienteDRPlaca10;

    //Cliente RUC-DNI
    public static String getclienteId10;
    public static String getclienteRUC10;
    public static String getclienteRZ10;
    public static String getclienteDR10;

    public static final String BASE_URL = "http://192.168.1.4:8081/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
