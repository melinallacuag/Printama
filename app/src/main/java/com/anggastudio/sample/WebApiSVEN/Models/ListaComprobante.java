package com.anggastudio.sample.WebApiSVEN.Models;

public class ListaComprobante {

    private String fechaEmision;
    private String nroRUC;
    private String razonSocial;
    private Double totalMonto;
    private String anulado;

    public ListaComprobante(String fechaEmision, String nroRUC, String razonSocial, Double totalMonto, String anulado) {
        this.fechaEmision = fechaEmision;
        this.nroRUC = nroRUC;
        this.razonSocial = razonSocial;
        this.totalMonto = totalMonto;
        this.anulado = anulado;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNroRUC() {
        return nroRUC;
    }

    public void setNroRUC(String nroRUC) {
        this.nroRUC = nroRUC;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Double getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(Double totalMonto) {
        this.totalMonto = totalMonto;
    }

    public String getAnulado() {
        return anulado;
    }

    public void setAnulado(String anulado) {
        this.anulado = anulado;
    }
}
