package com.fincas.gestion.service;

import com.fincas.gestion.model.Inquilino;
import com.fincas.gestion.model.Local;
import com.fincas.gestion.model.Piso;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InquilinoService {

    private final Map<String, Inquilino> inquilinos = new LinkedHashMap<>();
    private final InmuebleService inmuebleService;

    public InquilinoService(InmuebleService inmuebleService) {
        this.inmuebleService = inmuebleService;
    }

    public void registrarInquilino(Inquilino inquilino) {
        if (inquilinos.containsKey(inquilino.getId())) {
            throw new IllegalArgumentException("Ya existe un inquilino con ID: " + inquilino.getId());
        }
        if (!inquilino.validarGarantia()) {
            throw new IllegalStateException("El inquilino no presenta garantías válidas.");
        }
        inquilinos.put(inquilino.getId(), inquilino);
    }

    public Optional<Inquilino> buscarInquilino(String id) {
        return Optional.ofNullable(inquilinos.get(id));
    }

    public Optional<Inquilino> buscarInquilinoPorDNI(String dni) {
        return inquilinos.values().stream()
                .filter(i -> i.getDni().equalsIgnoreCase(dni))
                .findFirst();
    }

    public List<Inquilino> listarInquilinos() {
        return new ArrayList<>(inquilinos.values());
    }

    public List<Inquilino> listarInquilinosActivos() {
        return inquilinos.values().stream()
                .filter(Inquilino::isActivo)
                .collect(Collectors.toList());
    }

    public void actualizarInquilino(Inquilino inquilino) {
        if (!inquilinos.containsKey(inquilino.getId())) {
            throw new IllegalArgumentException("No existe inquilino con ID: " + inquilino.getId());
        }
        inquilinos.put(inquilino.getId(), inquilino);
    }

    public boolean darDeBajaInquilino(String id) {
        Inquilino inquilino = inquilinos.get(id);
        if (inquilino == null) return false;
        inquilino.setActivo(false);
        return true;
    }

    public void alquilarPiso(String pisoId, String inquilinoId, LocalDate fechaInicio, LocalDate fechaFin) {
        Piso piso = inmuebleService.buscarPiso(pisoId)
                .orElseThrow(() -> new IllegalArgumentException("Piso no encontrado: " + pisoId));
        Inquilino inquilino = buscarInquilino(inquilinoId)
                .orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado: " + inquilinoId));

        if (piso.isAlquilado()) {
            throw new IllegalStateException("El piso ya está alquilado.");
        }
        if (!piso.isGestionadoPorEmpresa()) {
            throw new IllegalStateException("El piso no está gestionado por la empresa.");
        }
        if (!inquilino.isActivo()) {
            throw new IllegalStateException("El inquilino no está activo.");
        }
        if (!inquilino.validarGarantia()) {
            throw new IllegalStateException("El inquilino no cumple los requisitos de garantía.");
        }

        piso.setInquilinoActual(inquilino);
        piso.setFechaInicioContrato(fechaInicio);
        piso.setFechaFinContrato(fechaFin);
        inmuebleService.actualizarPiso(piso);
    }

    public void desalquilarPiso(String pisoId) {
        Piso piso = inmuebleService.buscarPiso(pisoId)
                .orElseThrow(() -> new IllegalArgumentException("Piso no encontrado: " + pisoId));
        if (!piso.isAlquilado()) {
            throw new IllegalStateException("El piso no está actualmente alquilado.");
        }
        piso.setInquilinoActual(null);
        piso.setFechaInicioContrato(null);
        piso.setFechaFinContrato(null);
        inmuebleService.actualizarPiso(piso);
    }

    public void alquilarLocal(String localId, String inquilinoId, LocalDate fechaInicio, LocalDate fechaFin) {
        Local local = inmuebleService.buscarLocal(localId)
                .orElseThrow(() -> new IllegalArgumentException("Local no encontrado: " + localId));
        Inquilino inquilino = buscarInquilino(inquilinoId)
                .orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado: " + inquilinoId));

        if (local.isAlquilado()) {
            throw new IllegalStateException("El local ya está alquilado.");
        }
        if (!local.isGestionadoPorEmpresa()) {
            throw new IllegalStateException("El local no está gestionado por la empresa.");
        }
        if (!inquilino.isActivo()) {
            throw new IllegalStateException("El inquilino no está activo.");
        }
        if (!inquilino.validarGarantia()) {
            throw new IllegalStateException("El inquilino no cumple los requisitos de garantía.");
        }

        local.setInquilinoActual(inquilino);
        local.setFechaInicioContrato(fechaInicio);
        local.setFechaFinContrato(fechaFin);
        inmuebleService.actualizarLocal(local);
    }

    public void desalquilarLocal(String localId) {
        Local local = inmuebleService.buscarLocal(localId)
                .orElseThrow(() -> new IllegalArgumentException("Local no encontrado: " + localId));
        if (!local.isAlquilado()) {
            throw new IllegalStateException("El local no está actualmente alquilado.");
        }
        local.setInquilinoActual(null);
        local.setFechaInicioContrato(null);
        local.setFechaFinContrato(null);
        inmuebleService.actualizarLocal(local);
    }
}
