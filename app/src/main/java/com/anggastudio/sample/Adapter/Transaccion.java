package com.anggastudio.sample.Adapter;

public class Transaccion {

    private String cara;
    private String manguera;
    private String producto;
    private String precio;
    private String galones;
    private String soles;

    public Transaccion(String cara,String manguera,String producto,String precio,String galones,String soles){
        this.cara = cara;
        this.manguera = manguera;
        this.producto = producto;
        this.precio = precio;
        this.galones = galones;
        this.soles = soles;
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

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getGalones() {
        return galones;
    }

    public void setGalones(String galones) {
        this.galones = galones;
    }

    public String getSoles() {
        return soles;
    }

    public void setSoles(String soles) {
        this.soles = soles;
    }
}
