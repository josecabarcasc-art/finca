package com.fincas.gestion.model;


import java.time.LocalDate;

public class Gasto extends MovimientoBancario {

    public enum TipoGasto {
        REPARACION, LIMPIEZA, MANTENIMIENTO, SEGURO, IMPUESTO,
        SUMINISTROS, ADMINISTRACION, OBRA, OTRO
    }

    private String inmuebleId;
    private TipoGasto tipoGasto;
    private String proveedor;
    private String numeroFactura;
    private boolean deducibleIRPF;

    public Gasto(String id, LocalDate fecha, double monto, String descripcion,
                 CuentaBancaria cuentaBancaria, String inmuebleId,
                 TipoGasto tipoGasto, String proveedor, String numeroFactura, boolean deducibleIRPF) {
        super(id, fecha, monto, descripcion, cuentaBancaria);
        this.inmuebleId = inmuebleId;
        this.tipoGasto = tipoGasto;
        this.proveedor = proveedor;
        this.numeroFactura = numeroFactura;
        this.deducibleIRPF = deducibleIRPF;
    }

    @Override
    public String getTipoMovimiento() {
        return "GASTO";
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: " + tipoGasto + " | Inmueble: " + inmuebleId
                + " | Proveedor: " + proveedor + " | Factura: " + numeroFactura
                + " | IRPF deducible: " + (deducibleIRPF ? "Sí" : "No");
    }

    public String getInmuebleId() {
        return inmuebleId;
    }

    public void setInmuebleId(String inmuebleId) {
        this.inmuebleId = inmuebleId;
    }

    public TipoGasto getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public boolean isDeducibleIRPF() {
        return deducibleIRPF;
    }

    public void setDeducibleIRPF(boolean deducibleIRPF) {
        this.deducibleIRPF = deducibleIRPF;
    }
}
