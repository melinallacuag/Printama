package com.anggastudio.sample.WebApiSVEN.Models;

import java.math.BigInteger;

public class Optran {

    private Double tranID;
    private String cara;
    private String manguera;
    private String hora_tran;
    private String producto;
    private Double precio;
    private Double galones;
    private Double soles;

    public Double getTranID() {
        return tranID;
    }

    public void setTranID(Double tranID) {
        this.tranID = tranID;
    }

    public String getCara() {
        return cara;
    }

    public void setCara(String cara) {
        this.cara = cara;
    }

    public String getManguera() {
        return manguera;
    }

    public void setManguera(String manguera) {
        this.manguera = manguera;
    }

    public String getHora_tran() {
        return hora_tran;
    }

    public void setHora_tran(String hora_tran) {
        this.hora_tran = hora_tran;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getGalones() {
        return galones;
    }

    public void setGalones(Double galones) {
        this.galones = galones;
    }

    public Double getSoles() {
        return soles;
    }

    public void setSoles(Double soles) {
        this.soles = soles;
    }
}
