package com.fincas.gestion.service;

import com.fincas.gestion.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ContabilidadService {

    private final Map<String, CuentaBancaria> cuentas = new LinkedHashMap<>();
    private final List<MovimientoBancario> movimientos = new ArrayList<>();
    private int contadorId = 1;

    private String generarId(String prefijo) {
        return prefijo + "-" + String.format("%04d", contadorId++);
    }

    public void registrarCuenta(CuentaBancaria cuenta) {
        if (cuentas.containsKey(cuenta.getId())) {
            throw new IllegalArgumentException("Ya existe cuenta con ID: " + cuenta.getId());
        }
        cuentas.put(cuenta.getId(), cuenta);
    }

    public Optional<CuentaBancaria> buscarCuenta(String id) {
        return Optional.ofNullable(cuentas.get(id));
    }

    public List<CuentaBancaria> listarCuentas() {
        return new ArrayList<>(cuentas.values());
    }

    public Gasto registrarGasto(LocalDate fecha, double monto, String descripcion,
                                 String cuentaId, String inmuebleId,
                                 Gasto.TipoGasto tipoGasto, String proveedor,
                                 String numeroFactura, boolean deducibleIRPF) {
        CuentaBancaria cuenta = cuentas.get(cuentaId);
        if (cuenta == null) throw new IllegalArgumentException("Cuenta no encontrada: " + cuentaId);

        Gasto gasto = new Gasto(generarId("GAS"), fecha, monto, descripcion,
                cuenta, inmuebleId, tipoGasto, proveedor, numeroFactura, deducibleIRPF);
        cuenta.retirarFondos(monto);
        movimientos.add(gasto);
        return gasto;
    }

    public Ingreso registrarIngreso(LocalDate fecha, double monto, String descripcion,
                                     String cuentaId, String inmuebleId,
                                     String reciboId, Ingreso.TipoIngreso tipoIngreso) {
        CuentaBancaria cuenta = cuentas.get(cuentaId);
        if (cuenta == null) throw new IllegalArgumentException("Cuenta no encontrada: " + cuentaId);

        Ingreso ingreso = new Ingreso(generarId("ING"), fecha, monto, descripcion,
                cuenta, inmuebleId, reciboId, tipoIngreso);
        cuenta.ingresarFondos(monto);
        movimientos.add(ingreso);
        return ingreso;
    }

    public List<MovimientoBancario> listarMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    public List<MovimientoBancario> listarMovimientosPorCuenta(String cuentaId) {
        return movimientos.stream()
                .filter(m -> m.getCuentaBancaria().getId().equals(cuentaId))
                .collect(Collectors.toList());
    }

    public List<MovimientoBancario> listarMovimientosPorRango(LocalDate desde, LocalDate hasta) {
        return movimientos.stream()
                .filter(m -> !m.getFecha().isBefore(desde) && !m.getFecha().isAfter(hasta))
                .sorted(Comparator.comparing(MovimientoBancario::getFecha))
                .collect(Collectors.toList());
    }

    public List<Gasto> listarGastos() {
        return movimientos.stream()
                .filter(m -> m instanceof Gasto)
                .map(m -> (Gasto) m)
                .collect(Collectors.toList());
    }

    public List<Ingreso> listarIngresos() {
        return movimientos.stream()
                .filter(m -> m instanceof Ingreso)
                .map(m -> (Ingreso) m)
                .collect(Collectors.toList());
    }

    public Map<String, Double> generarInformeAnual(int anio) {
        Map<String, Double> informe = new LinkedHashMap<>();

        double totalIngresos = movimientos.stream()
                .filter(m -> m instanceof Ingreso && m.getFecha().getYear() == anio)
                .mapToDouble(MovimientoBancario::getMonto)
                .sum();

        double totalGastos = movimientos.stream()
                .filter(m -> m instanceof Gasto && m.getFecha().getYear() == anio)
                .mapToDouble(MovimientoBancario::getMonto)
                .sum();

        double gastosDeducibles = movimientos.stream()
                .filter(m -> m instanceof Gasto && m.getFecha().getYear() == anio)
                .map(m -> (Gasto) m)
                .filter(Gasto::isDeducibleIRPF)
                .mapToDouble(MovimientoBancario::getMonto)
                .sum();

        informe.put("Total Ingresos", totalIngresos);
        informe.put("Total Gastos", totalGastos);
        informe.put("Gastos Deducibles IRPF", gastosDeducibles);
        informe.put("Resultado Neto", totalIngresos - totalGastos);
        informe.put("Base Imponible IRPF", totalIngresos - gastosDeducibles);

        return informe;
    }

    public String generarInformeAnualFormateado(int anio) {
        Map<String, Double> datos = generarInformeAnual(anio);
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════\n");
        sb.append("   INFORME FINANCIERO ANUAL - AÑO ").append(anio).append("\n");
        sb.append("═══════════════════════════════════════════════\n");
        datos.forEach((k, v) -> sb.append(String.format("  %-30s %10.2f €%n", k, v)));
        sb.append("═══════════════════════════════════════════════\n");
        return sb.toString();
    }

    public Map<String, Double> gastosPorInmueble(int anio) {
        Map<String, Double> mapa = new LinkedHashMap<>();
        movimientos.stream()
                .filter(m -> m instanceof Gasto && m.getFecha().getYear() == anio)
                .map(m -> (Gasto) m)
                .forEach(g -> mapa.merge(g.getInmuebleId(), g.getMonto(), Double::sum));
        return mapa;
    }

    public Map<String, Double> ingresosPorInmueble(int anio) {
        Map<String, Double> mapa = new LinkedHashMap<>();
        movimientos.stream()
                .filter(m -> m instanceof Ingreso && m.getFecha().getYear() == anio)
                .map(m -> (Ingreso) m)
                .forEach(i -> mapa.merge(i.getInmuebleId(), i.getMonto(), Double::sum));
        return mapa;
    }
}
