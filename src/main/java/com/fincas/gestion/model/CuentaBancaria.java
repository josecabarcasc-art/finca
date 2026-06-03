package com.fincas.gestion.model;


public class CuentaBancaria {

    private String id;
    private String banco;
    private String numeroCuenta;
    private String iban;
    private String titular;
    private double saldo;

    public CuentaBancaria(String id, String banco, String numeroCuenta, String iban,
                          String titular, double saldo) {
        this.id = id;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.iban = iban;
        this.titular = titular;
        this.saldo = saldo;
    }

    public void ingresarFondos(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("El monto debe ser positivo.");
        this.saldo += monto;
    }

    public void retirarFondos(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("El monto debe ser positivo.");
        this.saldo -= monto;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + banco + " | IBAN: " + iban
                + " | Titular: " + titular + " | Saldo: " + String.format("%.2f", saldo) + "€";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
