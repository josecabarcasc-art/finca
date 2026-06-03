package com.fincas.gestion.model;


import java.time.LocalDate;

public class Inquilino {

    public enum Sexo { MASCULINO, FEMENINO, OTRO }
    public enum TipoGarantia { NOMINA, AVAL, CONTRATO, AVALADO_POR_PERSONA }

    private String id;
    private String nombre;
    private String apellidos;
    private String dni;
    private int edad;
    private Sexo sexo;
    private String telefono;
    private String email;
    private String rutaFotografia;
    private TipoGarantia tipoGarantia;
    private String datosGarantia;
    private LocalDate fechaAlta;
    private boolean activo;

    public Inquilino(String id, String nombre, String apellidos, String dni, int edad,
                     Sexo sexo, String telefono, String email, String rutaFotografia,
                     TipoGarantia tipoGarantia, String datosGarantia) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.edad = edad;
        this.sexo = sexo;
        this.telefono = telefono;
        this.email = email;
        this.rutaFotografia = rutaFotografia;
        this.tipoGarantia = tipoGarantia;
        this.datosGarantia = datosGarantia;
        this.fechaAlta = LocalDate.now();
        this.activo = true;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public String getIdentificacion() {
        return getNombreCompleto() + " | DNI: " + dni;
    }

    public boolean validarDNI() {
        if (dni == null || dni.length() != 9) return false;
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        try {
            int numero = Integer.parseInt(dni.substring(0, 8));
            char letraEsperada = letras.charAt(numero % 23);
            return Character.toUpperCase(dni.charAt(8)) == letraEsperada;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validarGarantia() {
        if (tipoGarantia == null) return false;
        if (datosGarantia == null || datosGarantia.isBlank()) return false;
        return switch (tipoGarantia) {
            case NOMINA -> datosGarantia.contains("empresa") || !datosGarantia.isBlank();
            case AVAL -> !datosGarantia.isBlank();
            case CONTRATO -> !datosGarantia.isBlank();
            case AVALADO_POR_PERSONA -> !datosGarantia.isBlank();
        };
    }

    @Override
    public String toString() {
        return "[" + id + "] " + getIdentificacion() + " | " + edad + " años | "
                + sexo + " | Garantía: " + tipoGarantia + " | Alta: " + fechaAlta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRutaFotografia() {
        return rutaFotografia;
    }

    public void setRutaFotografia(String rutaFotografia) {
        this.rutaFotografia = rutaFotografia;
    }

    public TipoGarantia getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(TipoGarantia tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }

    public String getDatosGarantia() {
        return datosGarantia;
    }

    public void setDatosGarantia(String datosGarantia) {
        this.datosGarantia = datosGarantia;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
