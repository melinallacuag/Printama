package com.anggastudio.sample.WebApiSVEN.Models;

public class VProducto {

    private String descripcion;
    private String cantidad;
    private String soles;
    private String descuentos;

    public VProducto(String descripcion, String cantidad, String soles, String descuentos) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.soles = soles;
        this.descuentos = descuentos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getSoles() {
        return soles;
    }

    public void setSoles(String soles) {
        this.soles = soles;
    }

    public String getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(String descuentos) {
        this.descuentos = descuentos;
    }
}
