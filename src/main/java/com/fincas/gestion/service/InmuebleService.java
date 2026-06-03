package com.fincas.gestion.service;

import com.fincas.gestion.model.Edificio;
import com.fincas.gestion.model.Inmueble;
import com.fincas.gestion.model.Local;
import com.fincas.gestion.model.Piso;

import java.util.*;
import java.util.stream.Collectors;

public class InmuebleService {

    private final Map<String, Edificio> edificios = new LinkedHashMap<>();
    private final Map<String, Piso> pisos = new LinkedHashMap<>();
    private final Map<String, Local> locales = new LinkedHashMap<>();

    public void registrarEdificio(Edificio edificio) {
        if (edificios.containsKey(edificio.getId())) {
            throw new IllegalArgumentException("Ya existe un edificio con ID: " + edificio.getId());
        }
        edificios.put(edificio.getId(), edificio);
    }

    public Optional<Edificio> buscarEdificio(String id) {
        return Optional.ofNullable(edificios.get(id));
    }

    public List<Edificio> listarEdificios() {
        return new ArrayList<>(edificios.values());
    }

    public void actualizarEdificio(Edificio edificio) {
        if (!edificios.containsKey(edificio.getId())) {
            throw new IllegalArgumentException("No existe edificio con ID: " + edificio.getId());
        }
        edificios.put(edificio.getId(), edificio);
    }

    public boolean eliminarEdificio(String id) {
        Edificio edificio = edificios.get(id);
        if (edificio == null) return false;
        if (!edificio.getPisos().isEmpty() || !edificio.getLocales().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un edificio que tiene pisos o locales asociados.");
        }
        edificios.remove(id);
        return true;
    }

    public void registrarPiso(Piso piso) {
        if (pisos.containsKey(piso.getId())) {
            throw new IllegalArgumentException("Ya existe un piso con ID: " + piso.getId());
        }
        if (piso.getEdificioId() != null) {
            Edificio edificio = edificios.get(piso.getEdificioId());
            if (edificio == null) {
                throw new IllegalArgumentException("Edificio no encontrado: " + piso.getEdificioId());
            }
            edificio.agregarPiso(piso);
        }
        pisos.put(piso.getId(), piso);
    }

    public Optional<Piso> buscarPiso(String id) {
        return Optional.ofNullable(pisos.get(id));
    }

    public List<Piso> listarPisos() {
        return new ArrayList<>(pisos.values());
    }

    public List<Piso> listarPisosPorEdificio(String edificioId) {
        return pisos.values().stream()
                .filter(p -> edificioId.equals(p.getEdificioId()))
                .collect(Collectors.toList());
    }

    public List<Piso> listarPisosLibres() {
        return pisos.values().stream()
                .filter(p -> !p.isAlquilado() && p.isGestionadoPorEmpresa())
                .collect(Collectors.toList());
    }

    public void actualizarPiso(Piso piso) {
        if (!pisos.containsKey(piso.getId())) {
            throw new IllegalArgumentException("No existe piso con ID: " + piso.getId());
        }
        pisos.put(piso.getId(), piso);
    }

    public boolean eliminarPiso(String id) {
        Piso piso = pisos.get(id);
        if (piso == null) return false;
        if (piso.isAlquilado()) {
            throw new IllegalStateException("No se puede eliminar un piso alquilado.");
        }
        if (piso.getEdificioId() != null) {
            Edificio edificio = edificios.get(piso.getEdificioId());
            if (edificio != null) edificio.eliminarPiso(id);
        }
        pisos.remove(id);
        return true;
    }

    public void registrarLocal(Local local) {
        if (locales.containsKey(local.getId())) {
            throw new IllegalArgumentException("Ya existe un local con ID: " + local.getId());
        }
        if (local.getEdificioId() != null) {
            Edificio edificio = edificios.get(local.getEdificioId());
            if (edificio == null) {
                throw new IllegalArgumentException("Edificio no encontrado: " + local.getEdificioId());
            }
            edificio.agregarLocal(local);
        }
        locales.put(local.getId(), local);
    }

    public Optional<Local> buscarLocal(String id) {
        return Optional.ofNullable(locales.get(id));
    }

    public List<Local> listarLocales() {
        return new ArrayList<>(locales.values());
    }

    public List<Local> listarLocalesPorEdificio(String edificioId) {
        return locales.values().stream()
                .filter(l -> edificioId.equals(l.getEdificioId()))
                .collect(Collectors.toList());
    }

    public List<Local> listarLocalesLibres() {
        return locales.values().stream()
                .filter(l -> !l.isAlquilado() && l.isGestionadoPorEmpresa())
                .collect(Collectors.toList());
    }

    public void actualizarLocal(Local local) {
        if (!locales.containsKey(local.getId())) {
            throw new IllegalArgumentException("No existe local con ID: " + local.getId());
        }
        locales.put(local.getId(), local);
    }

    public boolean eliminarLocal(String id) {
        Local local = locales.get(id);
        if (local == null) return false;
        if (local.isAlquilado()) {
            throw new IllegalStateException("No se puede eliminar un local alquilado.");
        }
        if (local.getEdificioId() != null) {
            Edificio edificio = edificios.get(local.getEdificioId());
            if (edificio != null) edificio.eliminarLocal(id);
        }
        locales.remove(id);
        return true;
    }

    public List<Inmueble> listarTodosLosInmuebles() {
        List<Inmueble> todos = new ArrayList<>();
        todos.addAll(edificios.values());
        todos.addAll(pisos.values());
        todos.addAll(locales.values());
        return todos;
    }
}
