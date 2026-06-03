package com.fincas.gestion.model;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Recibo {

    public enum EstadoRecibo { PENDIENTE, COBRADO, ANULADO }

    private String id;
    private String inmuebleId;
    private String inquilinoId;
    private int secuenciaInmueble;
    private LocalDate fechaEmision;
    private LocalDate fechaCobro;
    private EstadoRecibo estado;

    private Map<String, Double> conceptosObligatorios;
    private Map<String, Double> conceptosOpcionales;

    public Recibo(String id, String inmuebleId, String inquilinoId, int secuenciaInmueble, LocalDate fechaEmision) {
        this.id = id;
        this.inmuebleId = inmuebleId;
        this.inquilinoId = inquilinoId;
        this.secuenciaInmueble = secuenciaInmueble;
        this.fechaEmision = fechaEmision;
        this.estado = EstadoRecibo.PENDIENTE;
        this.conceptosObligatorios = new LinkedHashMap<>();
        this.conceptosOpcionales = new LinkedHashMap<>();

        conceptosObligatorios.put("Renta", 0.0);
        conceptosOpcionales.put("Agua", 0.0);
        conceptosOpcionales.put("Luz", 0.0);
        conceptosOpcionales.put("IPC", 0.0);
        conceptosOpcionales.put("Portería", 0.0);
        conceptosOpcionales.put("IVA", 0.0);
    }

    public void setConceptoObligatorio(String concepto, double valor) {
        conceptosObligatorios.put(concepto, valor);
    }

    public void setConceptoOpcional(String concepto, double valor) {
        conceptosOpcionales.put(concepto, valor);
    }

    public double getTotalObligatorio() {
        return conceptosObligatorios.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalOpcional() {
        return conceptosOpcionales.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotal() {
        return getTotalObligatorio() + getTotalOpcional();
    }

    public void marcarCobrado(LocalDate fechaCobro) {
        this.estado = EstadoRecibo.COBRADO;
        this.fechaCobro = fechaCobro;
    }

    public void anular() {
        this.estado = EstadoRecibo.ANULADO;
    }

    public Recibo clonarParaMesSiguiente(String nuevoId, int nuevaSecuencia) {
        LocalDate nuevaFecha = fechaEmision.plusMonths(1);
        Recibo clon = new Recibo(nuevoId, inmuebleId, inquilinoId, nuevaSecuencia, nuevaFecha);
        clon.conceptosObligatorios = new LinkedHashMap<>(this.conceptosObligatorios);
        clon.conceptosOpcionales = new LinkedHashMap<>(this.conceptosOpcionales);
        clon.estado = EstadoRecibo.PENDIENTE;
        clon.fechaCobro = null;
        return clon;
    }

    public String imprimirRecibo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("  RECIBO DE ALQUILER Nº ").append(secuenciaInmueble).append(" | ID: ").append(id).append("\n");
        sb.append("  Fecha emisión: ").append(fechaEmision.format(fmt)).append("\n");
        sb.append("  Inmueble: ").append(inmuebleId).append(" | Inquilino: ").append(inquilinoId).append("\n");
        sb.append("  Estado: ").append(estado).append("\n");
        sb.append("───────────────────────────────────────────────\n");
        sb.append("  CONCEPTOS:\n");

        conceptosObligatorios.forEach((k, v) -> {
            if (v != 0.0) {
                sb.append(String.format("  %-20s %10.2f €%n", k, v));
            }
        });

        conceptosOpcionales.forEach((k, v) -> {
            if (v != 0.0) {
                sb.append(String.format("  %-20s %10.2f €%n", k, v));
            }
        });

        sb.append("───────────────────────────────────────────────\n");
        sb.append(String.format("  %-20s %10.2f €%n", "TOTAL", getTotal()));
        if (estado == EstadoRecibo.COBRADO && fechaCobro != null) {
            sb.append("  Cobrado el: ").append(fechaCobro.format(fmt)).append("\n");
        }
        sb.append("═══════════════════════════════════════════════\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "[Recibo " + id + "] Inmueble: " + inmuebleId + " | Seq: " + secuenciaInmueble
                + " | Fecha: " + fechaEmision + " | Total: " + String.format("%.2f", getTotal())
                + "€ | Estado: " + estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInmuebleId() {
        return inmuebleId;
    }

    public void setInmuebleId(String inmuebleId) {
        this.inmuebleId = inmuebleId;
    }

    public String getInquilinoId() {
        return inquilinoId;
    }

    public void setInquilinoId(String inquilinoId) {
        this.inquilinoId = inquilinoId;
    }

    public int getSecuenciaInmueble() {
        return secuenciaInmueble;
    }

    public void setSecuenciaInmueble(int secuenciaInmueble) {
        this.secuenciaInmueble = secuenciaInmueble;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(LocalDate fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public EstadoRecibo getEstado() {
        return estado;
    }

    public void setEstado(EstadoRecibo estado) {
        this.estado = estado;
    }
}
