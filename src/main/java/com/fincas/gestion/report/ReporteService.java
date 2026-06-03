package com.fincas.gestion.report;

import com.fincas.gestion.model.*;
import com.fincas.gestion.service.ContabilidadService;
import com.fincas.gestion.service.InmuebleService;
import com.fincas.gestion.service.InquilinoService;
import com.fincas.gestion.service.ReciboService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteService {

    private final InmuebleService inmuebleService;
    private final InquilinoService inquilinoService;
    private final ReciboService reciboService;
    private final ContabilidadService contabilidadService;

    public ReporteService(InmuebleService inmuebleService, InquilinoService inquilinoService,
                          ReciboService reciboService, ContabilidadService contabilidadService) {
        this.inmuebleService = inmuebleService;
        this.inquilinoService = inquilinoService;
        this.reciboService = reciboService;
        this.contabilidadService = contabilidadService;
    }

    public String reporteInquilinosPorFechaAlta() {
        List<Inquilino> ordenados = inquilinoService.listarInquilinos().stream()
                .sorted(Comparator.comparing(Inquilino::getFechaAlta))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("   INQUILINOS ORDENADOS POR FECHA DE ALTA\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        if (ordenados.isEmpty()) {
            sb.append("  No hay inquilinos registrados.\n");
        } else {
            ordenados.forEach(i -> sb.append("  ").append(i).append("\n"));
        }
        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }

    public String reporteInquilinosMorosos(LocalDate desde, LocalDate hasta) {
        List<Recibo> pendientes = reciboService.listarRecibosPendientesEnRango(desde, hasta);
        Set<String> inquilinosConDeuda = pendientes.stream()
                .map(Recibo::getInquilinoId)
                .collect(Collectors.toSet());

        List<Inquilino> morosos = inquilinoService.listarInquilinos().stream()
                .filter(i -> inquilinosConDeuda.contains(i.getId()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("   INQUILINOS MOROSOS (" + desde + " → " + hasta + ")\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        if (morosos.isEmpty()) {
            sb.append("  No hay inquilinos morosos en el período indicado.\n");
        } else {
            morosos.forEach(i -> {
                double deuda = pendientes.stream()
                        .filter(r -> r.getInquilinoId().equals(i.getId()))
                        .mapToDouble(Recibo::getTotal)
                        .sum();
                sb.append(String.format("  %-35s Deuda: %10.2f €%n", i.getIdentificacion(), deuda));
            });
        }
        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }

    public String reporteInquilinosAlDia(LocalDate desde, LocalDate hasta) {
        List<Recibo> pendientes = reciboService.listarRecibosPendientesEnRango(desde, hasta);
        Set<String> inquilinosConDeuda = pendientes.stream()
                .map(Recibo::getInquilinoId)
                .collect(Collectors.toSet());

        List<Inquilino> alDia = inquilinoService.listarInquilinosActivos().stream()
                .filter(i -> !inquilinosConDeuda.contains(i.getId()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("   INQUILINOS AL DÍA (" + desde + " → " + hasta + ")\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        if (alDia.isEmpty()) {
            sb.append("  No hay inquilinos al día en el período indicado.\n");
        } else {
            alDia.forEach(i -> sb.append("  ").append(i.getIdentificacion()).append("\n"));
        }
        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }

    public String reporteInventarioInmuebles() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("   INVENTARIO COMPLETO DE INMUEBLES\n");
        sb.append("═══════════════════════════════════════════════════════════\n");

        List<Edificio> edificios = inmuebleService.listarEdificios();
        if (edificios.isEmpty()) {
            sb.append("  No hay edificios registrados.\n");
        } else {
            edificios.forEach(e -> {
                sb.append("\n  ► ").append(e.getResumen()).append("\n");
                sb.append("    Dirección: ").append(e.getDireccionCompleta()).append("\n");

                List<Piso> pisos = inmuebleService.listarPisosPorEdificio(e.getId());
                if (!pisos.isEmpty()) {
                    sb.append("    PISOS:\n");
                    pisos.forEach(p -> sb.append("      - ").append(p).append("\n"));
                }

                List<Local> locales = inmuebleService.listarLocalesPorEdificio(e.getId());
                if (!locales.isEmpty()) {
                    sb.append("    LOCALES:\n");
                    locales.forEach(l -> sb.append("      - ").append(l).append("\n"));
                }
            });
        }

        List<Piso> pisosSinEdificio = inmuebleService.listarPisos().stream()
                .filter(p -> p.getEdificioId() == null)
                .collect(Collectors.toList());
        if (!pisosSinEdificio.isEmpty()) {
            sb.append("\n  PISOS INDEPENDIENTES:\n");
            pisosSinEdificio.forEach(p -> sb.append("  - ").append(p).append("\n"));
        }

        List<Local> localesSinEdificio = inmuebleService.listarLocales().stream()
                .filter(l -> l.getEdificioId() == null)
                .collect(Collectors.toList());
        if (!localesSinEdificio.isEmpty()) {
            sb.append("\n  LOCALES INDEPENDIENTES:\n");
            localesSinEdificio.forEach(l -> sb.append("  - ").append(l).append("\n"));
        }

        sb.append("\n═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }

    public String reporteRecibosPendientes(LocalDate desde, LocalDate hasta) {
        List<Recibo> pendientes = reciboService.listarRecibosPendientesEnRango(desde, hasta);

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("   RECIBOS PENDIENTES (" + desde + " → " + hasta + ")\n");
        sb.append("═══════════════════════════════════════════════════════════\n");

        if (pendientes.isEmpty()) {
            sb.append("  No hay recibos pendientes en el período indicado.\n");
        } else {
            double totalPendiente = 0;
            for (Recibo r : pendientes) {
                sb.append("  ").append(r).append("\n");
                totalPendiente += r.getTotal();
            }
            sb.append("───────────────────────────────────────────────────────────\n");
            sb.append(String.format("  Total pendiente: %10.2f €%n", totalPendiente));
        }
        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }

    public String reporteFinancieroAnual(int anio) {
        return contabilidadService.generarInformeAnualFormateado(anio);
    }
}
