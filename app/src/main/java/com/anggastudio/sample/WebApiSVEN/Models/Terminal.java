package com.anggastudio.sample.WebApiSVEN.Models;

public class Terminal {

    private String  terminalID;
    private String  imei;
    private String  fecha_Proceso;
    private Integer turno;
    private Boolean consulta_Sunat;
    private Boolean venta_Automatica;
    private Boolean venta_Playa;
    private Boolean venta_Tienda;
    private Boolean venta_Credito;
    private Boolean venta_Tarjeta;
    private Boolean venta_Gratuita;
    private Boolean venta_Serafin;

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getFecha_Proceso() {
        return fecha_Proceso;
    }

    public void setFecha_Proceso(String fecha_Proceso) {
        this.fecha_Proceso = fecha_Proceso;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public Boolean getConsulta_Sunat() {
        return consulta_Sunat;
    }

    public void setConsulta_Sunat(Boolean consulta_Sunat) {
        this.consulta_Sunat = consulta_Sunat;
    }

    public Boolean getVenta_Automatica() {
        return venta_Automatica;
    }

    public void setVenta_Automatica(Boolean venta_Automatica) {
        this.venta_Automatica = venta_Automatica;
    }

    public Boolean getVenta_Playa() {
        return venta_Playa;
    }

    public void setVenta_Playa(Boolean venta_Playa) {
        this.venta_Playa = venta_Playa;
    }

    public Boolean getVenta_Tienda() {
        return venta_Tienda;
    }

    public void setVenta_Tienda(Boolean venta_Tienda) {
        this.venta_Tienda = venta_Tienda;
    }

    public Boolean getVenta_Credito() {
        return venta_Credito;
    }

    public void setVenta_Credito(Boolean venta_Credito) {
        this.venta_Credito = venta_Credito;
    }

    public Boolean getVenta_Tarjeta() {
        return venta_Tarjeta;
    }

    public void setVenta_Tarjeta(Boolean venta_Tarjeta) {
        this.venta_Tarjeta = venta_Tarjeta;
    }

    public Boolean getVenta_Gratuita() {
        return venta_Gratuita;
    }

    public void setVenta_Gratuita(Boolean venta_Gratuita) {
        this.venta_Gratuita = venta_Gratuita;
    }

    public Boolean getVenta_Serafin() {
        return venta_Serafin;
    }

    public void setVenta_Serafin(Boolean venta_Serafin) {
        this.venta_Serafin = venta_Serafin;
    }
}
