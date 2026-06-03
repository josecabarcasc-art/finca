package com.fincas.gestion.model;


import java.time.LocalDate;

public class Ingreso extends MovimientoBancario {

    public enum TipoIngreso {
        COBRO_RECIBO, DEPOSITO_FIANZA, DEVOLUCION, SUBVENCIÓN, OTRO
    }

    private String inmuebleId;
    private String reciboId;
    private TipoIngreso tipoIngreso;

    public Ingreso(String id, LocalDate fecha, double monto, String descripcion,
                   CuentaBancaria cuentaBancaria, String inmuebleId,
                   String reciboId, TipoIngreso tipoIngreso) {
        super(id, fecha, monto, descripcion, cuentaBancaria);
        this.inmuebleId = inmuebleId;
        this.reciboId = reciboId;
        this.tipoIngreso = tipoIngreso;
    }

    @Override
    public String getTipoMovimiento() {
        return "INGRESO";
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: " + tipoIngreso + " | Inmueble: " + inmuebleId
                + " | Recibo: " + (reciboId != null ? reciboId : "N/A");
    }

    public String getInmuebleId() {
        return inmuebleId;
    }

    public void setInmuebleId(String inmuebleId) {
        this.inmuebleId = inmuebleId;
    }

    public String getReciboId() {
        return reciboId;
    }

    public void setReciboId(String reciboId) {
        this.reciboId = reciboId;
    }

    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(TipoIngreso tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }
}
