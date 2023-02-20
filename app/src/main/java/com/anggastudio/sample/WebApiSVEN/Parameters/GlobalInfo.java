package com.anggastudio.sample.WebApiSVEN.Parameters;

import com.anggastudio.sample.WebApiSVEN.Controllers.APIService;

public class GlobalInfo {

    public static String getName10;
    public static String getImei10;
    public static String getCara10;
    public static String getPistola10;
    public static String fecha10;
    public static Integer getCompanyID = 1;

    public static final String BASE_URL = "http://192.168.1.6:8081/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
