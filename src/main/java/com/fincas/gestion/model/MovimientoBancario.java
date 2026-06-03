package com.fincas.gestion.model;


import java.time.LocalDate;

public abstract class MovimientoBancario {

    private String id;
    private LocalDate fecha;
    private double monto;
    private String descripcion;
    private CuentaBancaria cuentaBancaria;

    public MovimientoBancario(String id, LocalDate fecha, double monto,
                              String descripcion, CuentaBancaria cuentaBancaria) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.descripcion = descripcion;
        this.cuentaBancaria = cuentaBancaria;
    }

    public abstract String getTipoMovimiento();

    @Override
    public String toString() {
        return "[" + getTipoMovimiento() + "] " + fecha + " | " + String.format("%.2f", monto)
                + "€ | " + descripcion + " | Cuenta: " + cuentaBancaria.getIban();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }
}
