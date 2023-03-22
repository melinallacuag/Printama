package com.anggastudio.sample.WebApiSVEN.Models;

public class VTipoPago {
    private String tipoPago;
    private String MontoTotal;

    public VTipoPago(String tipoPago, String montoTotal) {
        this.tipoPago = tipoPago;
        MontoTotal = montoTotal;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        MontoTotal = montoTotal;
    }
}
