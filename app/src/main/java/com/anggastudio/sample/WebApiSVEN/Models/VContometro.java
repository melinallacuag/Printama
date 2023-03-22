package com.anggastudio.sample.WebApiSVEN.Models;

public class VContometro {

    private String lado;
    private String producto;
    private String cantidadI;
    private String cantidadF;
    private String galones;

    public VContometro(String lado, String producto, String cantidadI, String cantidadF, String galones) {
        this.lado = lado;
        this.producto = producto;
        this.cantidadI = cantidadI;
        this.cantidadF = cantidadF;
        this.galones = galones;
    }

    public String getLado() {
        return lado;
    }

    public void setLado(String lado) {
        this.lado = lado;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidadI() {
        return cantidadI;
    }

    public void setCantidadI(String cantidadI) {
        this.cantidadI = cantidadI;
    }

    public String getCantidadF() {
        return cantidadF;
    }

    public void setCantidadF(String cantidadF) {
        this.cantidadF = cantidadF;
    }

    public String getGalones() {
        return galones;
    }

    public void setGalones(String galones) {
        this.galones = galones;
    }
}
