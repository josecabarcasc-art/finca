package com.fincas.gestion.service;

import com.fincas.gestion.model.Recibo;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ReciboService {

    private final Map<String, Recibo> recibos = new LinkedHashMap<>();
    private final Map<String, AtomicInteger> secuenciasPorInmueble = new HashMap<>();
    private int contadorId = 1;

    private String generarId() {
        return "REC-" + String.format("%04d", contadorId++);
    }

    private int siguienteSecuencia(String inmuebleId) {
        secuenciasPorInmueble.putIfAbsent(inmuebleId, new AtomicInteger(0));
        return secuenciasPorInmueble.get(inmuebleId).incrementAndGet();
    }

    public Recibo generarRecibo(String inmuebleId, String inquilinoId, LocalDate fechaEmision,
                                 double renta, double agua, double luz, double ipc,
                                 double porteria, double iva) {
        String id = generarId();
        int seq = siguienteSecuencia(inmuebleId);
        Recibo recibo = new Recibo(id, inmuebleId, inquilinoId, seq, fechaEmision);
        recibo.setConceptoObligatorio("Renta", renta);
        recibo.setConceptoOpcional("Agua", agua);
        recibo.setConceptoOpcional("Luz", luz);
        recibo.setConceptoOpcional("IPC", ipc);
        recibo.setConceptoOpcional("Portería", porteria);
        recibo.setConceptoOpcional("IVA", iva);
        recibos.put(id, recibo);
        return recibo;
    }

    public Recibo clonarUltimoRecibo(String inmuebleId) {
        Recibo ultimo = recibos.values().stream()
                .filter(r -> r.getInmuebleId().equals(inmuebleId))
                .max(Comparator.comparing(Recibo::getFechaEmision))
                .orElseThrow(() -> new IllegalStateException("No hay recibos previos para el inmueble: " + inmuebleId));

        String nuevoId = generarId();
        int nuevaSeq = siguienteSecuencia(inmuebleId);
        Recibo clon = ultimo.clonarParaMesSiguiente(nuevoId, nuevaSeq);
        recibos.put(nuevoId, clon);
        return clon;
    }

    public void inicializarConceptosEnLote(String inmuebleId, double renta, double agua,
                                            double luz, double porteria) {
        recibos.values().stream()
                .filter(r -> r.getInmuebleId().equals(inmuebleId)
                        && r.getEstado() == Recibo.EstadoRecibo.PENDIENTE)
                .forEach(r -> {
                    r.setConceptoObligatorio("Renta", renta);
                    r.setConceptoOpcional("Agua", agua);
                    r.setConceptoOpcional("Luz", luz);
                    r.setConceptoOpcional("Portería", porteria);
                });
    }

    public void modificarConceptoRecibo(String reciboId, boolean esObligatorio,
                                         String concepto, double nuevoValor) {
        Recibo recibo = recibos.get(reciboId);
        if (recibo == null) throw new IllegalArgumentException("Recibo no encontrado: " + reciboId);
        if (esObligatorio) {
            recibo.setConceptoObligatorio(concepto, nuevoValor);
        } else {
            recibo.setConceptoOpcional(concepto, nuevoValor);
        }
    }

    public void marcarCobrado(String reciboId, LocalDate fechaCobro) {
        Recibo recibo = recibos.get(reciboId);
        if (recibo == null) throw new IllegalArgumentException("Recibo no encontrado: " + reciboId);
        if (recibo.getEstado() == Recibo.EstadoRecibo.ANULADO) {
            throw new IllegalStateException("El recibo está anulado.");
        }
        recibo.marcarCobrado(fechaCobro);
    }

    public void anularRecibo(String reciboId) {
        Recibo recibo = recibos.get(reciboId);
        if (recibo == null) throw new IllegalArgumentException("Recibo no encontrado: " + reciboId);
        recibo.anular();
    }

    public Optional<Recibo> buscarRecibo(String id) {
        return Optional.ofNullable(recibos.get(id));
    }

    public List<Recibo> listarRecibosPorInmueble(String inmuebleId) {
        return recibos.values().stream()
                .filter(r -> r.getInmuebleId().equals(inmuebleId))
                .sorted(Comparator.comparing(Recibo::getFechaEmision))
                .collect(Collectors.toList());
    }

    public List<Recibo> listarRecibosPendientes() {
        return recibos.values().stream()
                .filter(r -> r.getEstado() == Recibo.EstadoRecibo.PENDIENTE)
                .sorted(Comparator.comparing(Recibo::getFechaEmision))
                .collect(Collectors.toList());
    }

    public List<Recibo> listarRecibosPendientesEnRango(LocalDate desde, LocalDate hasta) {
        return recibos.values().stream()
                .filter(r -> r.getEstado() == Recibo.EstadoRecibo.PENDIENTE
                        && !r.getFechaEmision().isBefore(desde)
                        && !r.getFechaEmision().isAfter(hasta))
                .sorted(Comparator.comparing(Recibo::getFechaEmision))
                .collect(Collectors.toList());
    }

    public List<Recibo> listarTodosRecibos() {
        return new ArrayList<>(recibos.values());
    }

    public double calcularSaldoPendientePorInmueble(String inmuebleId) {
        return recibos.values().stream()
                .filter(r -> r.getInmuebleId().equals(inmuebleId)
                        && r.getEstado() == Recibo.EstadoRecibo.PENDIENTE)
                .mapToDouble(Recibo::getTotal)
                .sum();
    }

    public String imprimirRecibo(String reciboId) {
        Recibo recibo = recibos.get(reciboId);
        if (recibo == null) return "Recibo no encontrado: " + reciboId;
        return recibo.imprimirRecibo();
    }
}
