package com.fincas.gestion.main;

import com.fincas.gestion.model.*;
import com.fincas.gestion.report.ReporteService;
import com.fincas.gestion.service.*;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final InmuebleService inmuebleService = new InmuebleService();
    private static final InquilinoService inquilinoService = new InquilinoService(inmuebleService);
    private static final ReciboService reciboService = new ReciboService();
    private static final ContabilidadService contabilidadService = new ContabilidadService();
    private static final ReporteService reporteService = new ReporteService(
            inmuebleService, inquilinoService, reciboService, contabilidadService);
    private static final Scanner sc = new Scanner(System.in).useLocale(Locale.US);

    public static void main(String[] args) {
        cargarDatosDePrueba();
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción");
            procesarOpcionPrincipal(opcion);
        } while (opcion != 0);
        System.out.println("\nSistema cerrado. Hasta pronto.");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   GESTIÓN DE FINCAS E INMUEBLES          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Gestión de Inmuebles                 ║");
        System.out.println("║  2. Gestión de Inquilinos                ║");
        System.out.println("║  3. Gestión de Recibos                   ║");
        System.out.println("║  4. Contabilidad                         ║");
        System.out.println("║  5. Reportes                             ║");
        System.out.println("║  0. Salir                                ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    private static void procesarOpcionPrincipal(int opcion) {
        switch (opcion) {
            case 1 -> menuInmuebles();
            case 2 -> menuInquilinos();
            case 3 -> menuRecibos();
            case 4 -> menuContabilidad();
            case 5 -> menuReportes();
            case 0 -> {}
            default -> System.out.println("Opción no válida.");
        }
    }

    // ─────────────────────── MENÚ INMUEBLES ────────────────────────

    private static void menuInmuebles() {
        int op;
        do {
            System.out.println("\n── GESTIÓN DE INMUEBLES ──");
            System.out.println("1. Listar todos los inmuebles");
            System.out.println("2. Listar edificios");
            System.out.println("3. Listar pisos libres");
            System.out.println("4. Listar locales libres");
            System.out.println("5. Registrar edificio");
            System.out.println("6. Registrar piso");
            System.out.println("7. Registrar local");
            System.out.println("8. Eliminar piso");
            System.out.println("9. Eliminar local");
            System.out.println("0. Volver");
            op = leerEntero("Opción");
            switch (op) {
                case 1 -> {
                    List<Inmueble> lista = inmuebleService.listarTodosLosInmuebles();
                    if(lista.isEmpty()) System.out.println("  No hay inmuebles.");
                    else lista.forEach(System.out::println);
                }
                case 2 -> {
                    List<Edificio> lista = inmuebleService.listarEdificios();
                    if(lista.isEmpty()) System.out.println("  No hay edificios.");
                    else lista.forEach(System.out::println);
                }
                case 3 -> {
                    List<Piso> lista = inmuebleService.listarPisosLibres();
                    if(lista.isEmpty()) System.out.println("  No hay pisos libres.");
                    else lista.forEach(System.out::println);
                }
                case 4 -> {
                    List<Local> lista = inmuebleService.listarLocalesLibres();
                    if(lista.isEmpty()) System.out.println("  No hay locales libres.");
                    else lista.forEach(System.out::println);
                }
                case 5 -> registrarEdificio();
                case 6 -> registrarPiso();
                case 7 -> registrarLocal();
                case 8 -> eliminarPiso();
                case 9 -> eliminarLocal();
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (op != 0);
    }

    private static void registrarEdificio() {
        System.out.println("\n── Registrar Edificio ──");
        String id = leerTexto("ID del edificio");
        String nombre = leerTexto("Nombre del edificio");
        String direccion = leerTexto("Dirección");
        String numero = leerTexto("Número");
        String cp = leerTexto("Código postal");
        String ciudad = leerTexto("Ciudad");
        String provincia = leerTexto("Provincia");
        String catastral = leerTexto("Referencia catastral");
        double superficie = leerDouble("Superficie (m²)");
        int plantas = leerEntero("Total de plantas");

        Edificio e = new Edificio(id, direccion, numero, cp, ciudad, provincia, catastral, superficie, nombre, plantas);
        try {
            inmuebleService.registrarEdificio(e);
            System.out.println("Edificio registrado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void registrarPiso() {
        System.out.println("\n── Registrar Piso ──");
        String id = leerTexto("ID del piso");
        String edificioId = leerTexto("ID del edificio (vacío si es independiente)");
        if (edificioId.isBlank()) edificioId = null;
        String direccion = leerTexto("Dirección");
        String numero = leerTexto("Número");
        String cp = leerTexto("Código postal");
        String ciudad = leerTexto("Ciudad");
        String provincia = leerTexto("Provincia");
        String catastral = leerTexto("Referencia catastral");
        double superficie = leerDouble("Superficie (m²)");
        int planta = leerEntero("Planta");
        String puerta = leerTexto("Puerta");
        int habitaciones = leerEntero("Habitaciones");
        int banos = leerEntero("Baños");
        boolean gestionado = leerBooleano("¿Gestionado por la empresa?");
        double renta = leerDouble("Renta mensual (€)");

        Piso p = new Piso(id, direccion, numero, cp, ciudad, provincia, catastral,
                superficie, edificioId, planta, puerta, habitaciones, banos, gestionado, renta);
        try {
            inmuebleService.registrarPiso(p);
            System.out.println("Piso registrado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void registrarLocal() {
        System.out.println("\n── Registrar Local ──");
        String id = leerTexto("ID del local");
        String edificioId = leerTexto("ID del edificio (vacío si es independiente)");
        if (edificioId.isBlank()) edificioId = null;
        String direccion = leerTexto("Dirección");
        String numero = leerTexto("Número");
        String cp = leerTexto("Código postal");
        String ciudad = leerTexto("Ciudad");
        String provincia = leerTexto("Provincia");
        String catastral = leerTexto("Referencia catastral");
        double superficie = leerDouble("Superficie (m²)");
        int numLocal = leerEntero("Número de local");
        System.out.println("Usos: COMERCIAL, OFICINA, ALMACEN, HOSTELERIA, OTRO");
        Local.UsoLocal uso = Local.UsoLocal.valueOf(leerTexto("Uso").toUpperCase());
        boolean gestionado = leerBooleano("¿Gestionado por la empresa?");
        double renta = leerDouble("Renta mensual (€)");
        boolean iva = leerBooleano("¿Aplica IVA?");

        Local l = new Local(id, direccion, numero, cp, ciudad, provincia, catastral,
                superficie, edificioId, numLocal, uso, gestionado, renta, iva);
        try {
            inmuebleService.registrarLocal(l);
            System.out.println("Local registrado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void eliminarPiso() {
        String id = leerTexto("ID del piso a eliminar");
        try {
            boolean ok = inmuebleService.eliminarPiso(id);
            System.out.println(ok ? "Piso eliminado." : "Piso no encontrado.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void eliminarLocal() {
        String id = leerTexto("ID del local a eliminar");
        try {
            boolean ok = inmuebleService.eliminarLocal(id);
            System.out.println(ok ? "Local eliminado." : "Local no encontrado.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // ─────────────────────── MENÚ INQUILINOS ───────────────────────

    private static void menuInquilinos() {
        int op;
        do {
            System.out.println("\n── GESTIÓN DE INQUILINOS ──");
            System.out.println("1. Listar inquilinos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por DNI");
            System.out.println("4. Registrar inquilino");
            System.out.println("5. Alquilar piso");
            System.out.println("6. Desalquilar piso");
            System.out.println("7. Alquilar local");
            System.out.println("8. Desalquilar local");
            System.out.println("9. Dar de baja inquilino");
            System.out.println("0. Volver");
            op = leerEntero("Opción");
            switch (op) {
                case 1 -> inquilinoService.listarInquilinos().forEach(System.out::println);
                case 2 -> {
                    String id = leerTexto("ID del inquilino");
                    inquilinoService.buscarInquilino(id).ifPresentOrElse(
                            System.out::println, () -> System.out.println("Inquilino no encontrado."));
                }
                case 3 -> {
                    String dni = leerTexto("DNI del inquilino");
                    inquilinoService.buscarInquilinoPorDNI(dni).ifPresentOrElse(
                            System.out::println, () -> System.out.println("Inquilino no encontrado."));
                }
                case 4 -> registrarInquilino();
                case 5 -> alquilarPiso();
                case 6 -> desalquilarPiso();
                case 7 -> alquilarLocal();
                case 8 -> desalquilarLocal();
                case 9 -> {
                    String id = leerTexto("ID del inquilino a dar de baja");
                    System.out.println(inquilinoService.darDeBajaInquilino(id)
                            ? "Inquilino dado de baja." : "Inquilino no encontrado.");
                }
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (op != 0);
    }

    private static void registrarInquilino() {
        System.out.println("\n── Registrar Inquilino ──");
        String id = leerTexto("ID del inquilino");
        String nombre = leerTexto("Nombre");
        String apellidos = leerTexto("Apellidos");
        String dni = leerTexto("DNI");
        int edad = leerEntero("Edad");
        System.out.println("Sexo: MASCULINO, FEMENINO, OTRO");
        Inquilino.Sexo sexo = Inquilino.Sexo.valueOf(leerTexto("Sexo").toUpperCase());
        String telefono = leerTexto("Teléfono");
        String email = leerTexto("Email");
        String foto = leerTexto("Ruta fotografía (vacío si no aplica)");
        System.out.println("Garantía: NOMINA, AVAL, CONTRATO, AVALADO_POR_PERSONA");
        Inquilino.TipoGarantia garantia = Inquilino.TipoGarantia.valueOf(leerTexto("Tipo garantía").toUpperCase());
        String datosGarantia = leerTexto("Datos de garantía");

        Inquilino inq = new Inquilino(id, nombre, apellidos, dni, edad, sexo,
                telefono, email, foto, garantia, datosGarantia);
        try {
            inquilinoService.registrarInquilino(inq);
            System.out.println("Inquilino registrado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void alquilarPiso() {
        String pisoId = leerTexto("ID del piso");
        String inquilinoId = leerTexto("ID del inquilino");
        LocalDate inicio = leerFecha("Fecha inicio contrato (yyyy-MM-dd)");
        LocalDate fin = leerFecha("Fecha fin contrato (yyyy-MM-dd)");
        try {
            inquilinoService.alquilarPiso(pisoId, inquilinoId, inicio, fin);
            System.out.println("Piso alquilado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void desalquilarPiso() {
        String pisoId = leerTexto("ID del piso a desalquilar");
        try {
            inquilinoService.desalquilarPiso(pisoId);
            System.out.println("Piso desalquilado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void alquilarLocal() {
        String localId = leerTexto("ID del local");
        String inquilinoId = leerTexto("ID del inquilino");
        LocalDate inicio = leerFecha("Fecha inicio contrato (yyyy-MM-dd)");
        LocalDate fin = leerFecha("Fecha fin contrato (yyyy-MM-dd)");
        try {
            inquilinoService.alquilarLocal(localId, inquilinoId, inicio, fin);
            System.out.println("Local alquilado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void desalquilarLocal() {
        String localId = leerTexto("ID del local a desalquilar");
        try {
            inquilinoService.desalquilarLocal(localId);
            System.out.println("Local desalquilado correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // ─────────────────────── MENÚ RECIBOS ──────────────────────────

    private static void menuRecibos() {
        int op;
        do {
            System.out.println("\n── GESTIÓN DE RECIBOS ──");
            System.out.println("1. Listar todos los recibos");
            System.out.println("2. Listar recibos por inmueble");
            System.out.println("3. Listar recibos pendientes");
            System.out.println("4. Generar recibo");
            System.out.println("5. Clonar último recibo del mes anterior");
            System.out.println("6. Modificar concepto de un recibo");
            System.out.println("7. Marcar recibo como cobrado");
            System.out.println("8. Anular recibo");
            System.out.println("9. Imprimir recibo");
            System.out.println("10. Saldo pendiente por inmueble");
            System.out.println("0. Volver");
            op = leerEntero("Opción");
            switch (op) {
                case 1 -> reciboService.listarTodosRecibos().forEach(System.out::println);
                case 2 -> {
                    String id = leerTexto("ID del inmueble");
                    reciboService.listarRecibosPorInmueble(id).forEach(System.out::println);
                }
                case 3 -> reciboService.listarRecibosPendientes().forEach(System.out::println);
                case 4 -> generarRecibo();
                case 5 -> clonarRecibo();
                case 6 -> modificarConceptoRecibo();
                case 7 -> marcarReciboCobrado();
                case 8 -> {
                    String id = leerTexto("ID del recibo a anular");
                    try {
                        reciboService.anularRecibo(id);
                        System.out.println("Recibo anulado.");
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
                case 9 -> {
                    String id = leerTexto("ID del recibo a imprimir");
                    System.out.println(reciboService.imprimirRecibo(id));
                }
                case 10 -> {
                    String id = leerTexto("ID del inmueble");
                    double saldo = reciboService.calcularSaldoPendientePorInmueble(id);
                    System.out.printf("Saldo pendiente para %s: %.2f €%n", id, saldo);
                }
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (op != 0);
    }

    private static void generarRecibo() {
        String inmuebleId = leerTexto("ID del inmueble");
        String inquilinoId = leerTexto("ID del inquilino");
        LocalDate fecha = leerFecha("Fecha de emisión (yyyy-MM-dd)");
        double renta = leerDouble("Renta (€)");
        double agua = leerDouble("Agua (€, 0 si no aplica)");
        double luz = leerDouble("Luz (€, 0 si no aplica)");
        double ipc = leerDouble("IPC (€, 0 si no aplica)");
        double porteria = leerDouble("Portería (€, 0 si no aplica)");
        double iva = leerDouble("IVA (€, 0 si no aplica)");
        Recibo r = reciboService.generarRecibo(inmuebleId, inquilinoId, fecha, renta, agua, luz, ipc, porteria, iva);
        System.out.println("Recibo generado: " + r.getId());
    }

    private static void clonarRecibo() {
        String inmuebleId = leerTexto("ID del inmueble para clonar último recibo");
        try {
            Recibo clon = reciboService.clonarUltimoRecibo(inmuebleId);
            System.out.println("Recibo clonado para el mes siguiente. Nuevo ID: " + clon.getId());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void modificarConceptoRecibo() {
        String reciboId = leerTexto("ID del recibo");
        boolean esObligatorio = leerBooleano("¿Es concepto obligatorio?");
        String concepto = leerTexto("Nombre del concepto (ej: Renta, Agua, Luz, IPC, Portería, IVA)");
        double valor = leerDouble("Nuevo valor (€)");
        try {
            reciboService.modificarConceptoRecibo(reciboId, esObligatorio, concepto, valor);
            System.out.println("Concepto actualizado.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void marcarReciboCobrado() {
        String reciboId = leerTexto("ID del recibo");
        LocalDate fecha = leerFecha("Fecha de cobro (yyyy-MM-dd)");
        try {
            reciboService.marcarCobrado(reciboId, fecha);
            System.out.println("Recibo marcado como cobrado.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // ─────────────────────── MENÚ CONTABILIDAD ─────────────────────

    private static void menuContabilidad() {
        int op;
        do {
            System.out.println("\n── CONTABILIDAD ──");
            System.out.println("1. Listar cuentas bancarias");
            System.out.println("2. Registrar cuenta bancaria");
            System.out.println("3. Registrar gasto");
            System.out.println("4. Registrar ingreso");
            System.out.println("5. Listar todos los movimientos");
            System.out.println("6. Listar movimientos por rango de fechas");
            System.out.println("7. Informe financiero anual");
            System.out.println("8. Gastos por inmueble en un año");
            System.out.println("9. Ingresos por inmueble en un año");
            System.out.println("0. Volver");
            op = leerEntero("Opción");
            switch (op) {
                case 1 -> contabilidadService.listarCuentas().forEach(System.out::println);
                case 2 -> registrarCuentaBancaria();
                case 3 -> registrarGasto();
                case 4 -> registrarIngreso();
                case 5 -> contabilidadService.listarMovimientos().forEach(System.out::println);
                case 6 -> {
                    LocalDate d = leerFecha("Desde (yyyy-MM-dd)");
                    LocalDate h = leerFecha("Hasta (yyyy-MM-dd)");
                    contabilidadService.listarMovimientosPorRango(d, h).forEach(System.out::println);
                }
                case 7 -> {
                    int anio = leerEntero("Año");
                    System.out.println(contabilidadService.generarInformeAnualFormateado(anio));
                }
                case 8 -> {
                    int anio = leerEntero("Año");
                    contabilidadService.gastosPorInmueble(anio).forEach(
                            (k, v) -> System.out.printf("  Inmueble %s: %.2f €%n", k, v));
                }
                case 9 -> {
                    int anio = leerEntero("Año");
                    contabilidadService.ingresosPorInmueble(anio).forEach(
                            (k, v) -> System.out.printf("  Inmueble %s: %.2f €%n", k, v));
                }
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (op != 0);
    }

    private static void registrarCuentaBancaria() {
        String id = leerTexto("ID de la cuenta");
        String banco = leerTexto("Nombre del banco");
        String numeroCuenta = leerTexto("Número de cuenta");
        String iban = leerTexto("IBAN");
        String titular = leerTexto("Titular");
        double saldo = leerDouble("Saldo inicial (€)");
        try {
            contabilidadService.registrarCuenta(new CuentaBancaria(id, banco, numeroCuenta, iban, titular, saldo));
            System.out.println("Cuenta registrada correctamente.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void registrarGasto() {
        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd)");
        double monto = leerDouble("Monto (€)");
        String descripcion = leerTexto("Descripción");
        String cuentaId = leerTexto("ID de la cuenta bancaria");
        String inmuebleId = leerTexto("ID del inmueble");
        System.out.println("Tipos: REPARACION, LIMPIEZA, MANTENIMIENTO, SEGURO, IMPUESTO, SUMINISTROS, ADMINISTRACION, OBRA, OTRO");
        Gasto.TipoGasto tipo = Gasto.TipoGasto.valueOf(leerTexto("Tipo de gasto").toUpperCase());
        String proveedor = leerTexto("Proveedor");
        String factura = leerTexto("Número de factura");
        boolean deducible = leerBooleano("¿Deducible IRPF?");
        try {
            Gasto g = contabilidadService.registrarGasto(fecha, monto, descripcion, cuentaId,
                    inmuebleId, tipo, proveedor, factura, deducible);
            System.out.println("Gasto registrado: " + g.getId());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void registrarIngreso() {
        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd)");
        double monto = leerDouble("Monto (€)");
        String descripcion = leerTexto("Descripción");
        String cuentaId = leerTexto("ID de la cuenta bancaria");
        String inmuebleId = leerTexto("ID del inmueble");
        String reciboId = leerTexto("ID del recibo (vacío si no aplica)");
        if (reciboId.isBlank()) reciboId = null;
        System.out.println("Tipos: COBRO_RECIBO, DEPOSITO_FIANZA, DEVOLUCION, SUBVENCION, OTRO");
        Ingreso.TipoIngreso tipo = Ingreso.TipoIngreso.valueOf(leerTexto("Tipo de ingreso").toUpperCase());
        try {
            Ingreso i = contabilidadService.registrarIngreso(fecha, monto, descripcion, cuentaId,
                    inmuebleId, reciboId, tipo);
            System.out.println("Ingreso registrado: " + i.getId());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // ─────────────────────── MENÚ REPORTES ─────────────────────────

    private static void menuReportes() {
        int op;
        do {
            System.out.println("\n── REPORTES ──");
            System.out.println("1. Inquilinos por fecha de alta");
            System.out.println("2. Inquilinos morosos en un período");
            System.out.println("3. Inquilinos al día en un período");
            System.out.println("4. Inventario completo de inmuebles");
            System.out.println("5. Recibos pendientes en un período");
            System.out.println("6. Informe financiero anual");
            System.out.println("0. Volver");
            op = leerEntero("Opción");
            switch (op) {
                case 1 -> System.out.println(reporteService.reporteInquilinosPorFechaAlta());
                case 2 -> {
                    LocalDate d = leerFecha("Desde (yyyy-MM-dd)");
                    LocalDate h = leerFecha("Hasta (yyyy-MM-dd)");
                    System.out.println(reporteService.reporteInquilinosMorosos(d, h));
                }
                case 3 -> {
                    LocalDate d = leerFecha("Desde (yyyy-MM-dd)");
                    LocalDate h = leerFecha("Hasta (yyyy-MM-dd)");
                    System.out.println(reporteService.reporteInquilinosAlDia(d, h));
                }
                case 4 -> System.out.println(reporteService.reporteInventarioInmuebles());
                case 5 -> {
                    LocalDate d = leerFecha("Desde (yyyy-MM-dd)");
                    LocalDate h = leerFecha("Hasta (yyyy-MM-dd)");
                    System.out.println(reporteService.reporteRecibosPendientes(d, h));
                }
                case 6 -> {
                    int anio = leerEntero("Año");
                    System.out.println(reporteService.reporteFinancieroAnual(anio));
                }
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (op != 0);
    }

    // ─────────────────────── DATOS DE PRUEBA ───────────────────────

    private static void cargarDatosDePrueba() {
        CuentaBancaria cuenta1 = new CuentaBancaria("CTA-001", "Banco Santander",
                "0049-2345-67-1234567890", "ES12 0049 2345 6712 3456 7890", "Inmobiliaria López S.L.", 50000.00);
        CuentaBancaria cuenta2 = new CuentaBancaria("CTA-002", "CaixaBank",
                "2100-3456-78-1234567890", "ES34 2100 3456 7812 3456 7890", "Inmobiliaria López S.L.", 20000.00);
        contabilidadService.registrarCuenta(cuenta1);
        contabilidadService.registrarCuenta(cuenta2);

        Edificio edificio1 = new Edificio("ED-001", "Calle Mayor", "15", "28001",
                "Madrid", "Madrid", "28900A001", 1200.0, "Edificio Central", 5);
        Edificio edificio2 = new Edificio("ED-002", "Avenida de la Constitución", "42", "41001",
                "Sevilla", "Sevilla", "41900B002", 900.0, "Residencial Sur", 4);
        inmuebleService.registrarEdificio(edificio1);
        inmuebleService.registrarEdificio(edificio2);

        Piso p1 = new Piso("P-001", "Calle Mayor", "15", "28001", "Madrid", "Madrid",
                "28900A001P1", 75.0, "ED-001", 1, "A", 2, 1, true, 850.00);
        Piso p2 = new Piso("P-002", "Calle Mayor", "15", "28001", "Madrid", "Madrid",
                "28900A001P2", 80.0, "ED-001", 2, "B", 3, 2, true, 950.00);
        Piso p3 = new Piso("P-003", "Calle Mayor", "15", "28001", "Madrid", "Madrid",
                "28900A001P3", 65.0, "ED-001", 3, "A", 2, 1, true, 600.0);
        Piso p4 = new Piso("P-004", "Avenida de la Constitución", "42", "41001", "Sevilla", "Sevilla",
                "41900B002P1", 90.0, "ED-002", 1, "A", 3, 2, true, 700.00);
        inmuebleService.registrarPiso(p1);
        inmuebleService.registrarPiso(p2);
        inmuebleService.registrarPiso(p3);
        inmuebleService.registrarPiso(p4);

        Local l1 = new Local("L-001", "Calle Mayor", "15", "28001", "Madrid", "Madrid",
                "28900A001L1", 120.0, "ED-001", 1, Local.UsoLocal.COMERCIAL, true, 1500.00, true);
        Local l2 = new Local("L-002", "Avenida de la Constitución", "42", "41001", "Sevilla", "Sevilla",
                "41900B002L1", 85.0, "ED-002", 1, Local.UsoLocal.OFICINA, true, 900.00, true);
        inmuebleService.registrarLocal(l1);
        inmuebleService.registrarLocal(l2);

        Inquilino inq1 = new Inquilino("INQ-001", "Carlos", "García Martínez", "12345678Z",
                35, Inquilino.Sexo.MASCULINO, "600111222", "carlos@email.com",
                "/fotos/carlos.jpg", Inquilino.TipoGarantia.NOMINA, "Empresa TechCorp S.A.");
        Inquilino inq2 = new Inquilino("INQ-002", "María", "López Sánchez", "87654321X",
                28, Inquilino.Sexo.FEMENINO, "600333444", "maria@email.com",
                "/fotos/maria.jpg", Inquilino.TipoGarantia.AVAL, "Banco Santander Aval Nº 12345");
        Inquilino inq3 = new Inquilino("INQ-003", "Antonio", "Ruiz Fernández", "11223344M",
                42, Inquilino.Sexo.MASCULINO, "600555666", "antonio@email.com",
                "/fotos/antonio.jpg", Inquilino.TipoGarantia.CONTRATO, "Contrato indefinido SEAT S.A.");
        Inquilino inq4 = new Inquilino("INQ-004", "Laura", "Pérez González", "44332211K",
                31, Inquilino.Sexo.FEMENINO, "600777888", "laura@email.com",
                "/fotos/laura.jpg", Inquilino.TipoGarantia.AVALADO_POR_PERSONA, "Juan Pérez DNI 55667788Q");
        inquilinoService.registrarInquilino(inq1);
        inquilinoService.registrarInquilino(inq2);
        inquilinoService.registrarInquilino(inq3);
        inquilinoService.registrarInquilino(inq4);

        inquilinoService.alquilarPiso("P-001", "INQ-001",
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 12, 31));
        inquilinoService.alquilarPiso("P-002", "INQ-002",
                LocalDate.of(2024, 3, 1), LocalDate.of(2026, 2, 28));
        inquilinoService.alquilarPiso("P-004", "INQ-003",
                LocalDate.of(2023, 6, 1), LocalDate.of(2025, 5, 31));
        inquilinoService.alquilarLocal("L-001", "INQ-004",
                LocalDate.of(2024, 2, 1), LocalDate.of(2027, 1, 31));

        Recibo r1 = reciboService.generarRecibo("P-001", "INQ-001",
                LocalDate.of(2025, 1, 1), 850.0, 30.0, 45.0, 0.0, 15.0, 0.0);
        Recibo r2 = reciboService.generarRecibo("P-001", "INQ-001",
                LocalDate.of(2025, 2, 1), 850.0, 28.0, 42.0, 0.0, 15.0, 0.0);
        Recibo r3 = reciboService.generarRecibo("P-002", "INQ-002",
                LocalDate.of(2025, 1, 1), 950.0, 35.0, 50.0, 0.0, 20.0, 0.0);
        Recibo r4 = reciboService.generarRecibo("P-004", "INQ-003",
                LocalDate.of(2025, 1, 1), 700.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Recibo r5 = reciboService.generarRecibo("L-001", "INQ-004",
                LocalDate.of(2025, 1, 1), 1500.0, 0.0, 0.0, 0.0, 0.0, 315.0);

        reciboService.marcarCobrado(r1.getId(), LocalDate.of(2025, 1, 5));
        reciboService.marcarCobrado(r3.getId(), LocalDate.of(2025, 1, 3));
        reciboService.marcarCobrado(r5.getId(), LocalDate.of(2025, 1, 4));

        contabilidadService.registrarIngreso(LocalDate.of(2025, 1, 5), 940.0,
                "Cobro recibo P-001 enero 2025", "CTA-001", "P-001", r1.getId(), Ingreso.TipoIngreso.COBRO_RECIBO);
        contabilidadService.registrarIngreso(LocalDate.of(2025, 1, 3), 1055.0,
                "Cobro recibo P-002 enero 2025", "CTA-001", "P-002", r3.getId(), Ingreso.TipoIngreso.COBRO_RECIBO);
        contabilidadService.registrarIngreso(LocalDate.of(2025, 1, 4), 1815.0,
                "Cobro recibo L-001 enero 2025 (con IVA)", "CTA-001", "L-001", r5.getId(), Ingreso.TipoIngreso.COBRO_RECIBO);

        contabilidadService.registrarGasto(LocalDate.of(2025, 1, 10), 350.0,
                "Reparación fontanería P-001", "CTA-001", "P-001",
                Gasto.TipoGasto.REPARACION, "Fontanería García", "FAC-001", true);
        contabilidadService.registrarGasto(LocalDate.of(2025, 1, 15), 200.0,
                "Limpieza zonas comunes ED-001", "CTA-001", "ED-001",
                Gasto.TipoGasto.LIMPIEZA, "Limpiezas Pro S.L.", "FAC-002", true);
        contabilidadService.registrarGasto(LocalDate.of(2025, 2, 5), 1200.0,
                "Seguro multirriesgo edificio ED-002", "CTA-002", "ED-002",
                Gasto.TipoGasto.SEGURO, "Mapfre Seguros", "FAC-003", true);

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  Sistema iniciado con datos de prueba precargados.");
        System.out.println("  Edificios: 2 | Pisos: 4 | Locales: 2");
        System.out.println("  Inquilinos: 4 | Recibos: 5 | Movimientos: 6");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    // ─────────────────────── UTILIDADES ────────────────────────────

    private static String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return sc.nextLine().trim();
    }

    private static int leerEntero(String etiqueta) {
        while (true) {
            try {
                System.out.print(etiqueta + ": ");
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Por favor, introduzca un número entero válido.");
            }
        }
    }

    private static double leerDouble(String etiqueta) {
        while (true) {
            try {
                System.out.print(etiqueta + ": ");
                double valor = sc.nextDouble();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Por favor, introduzca un número decimal válido.");
            }
        }
    }

    private static boolean leerBooleano(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + " (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("s") || resp.equals("si") || resp.equals("sí")) return true;
            if (resp.equals("n") || resp.equals("no")) return false;
            System.out.println("Por favor, responda 's' o 'n'.");
        }
    }

    private static LocalDate leerFecha(String etiqueta) {
        while (true) {
            try {
                System.out.print(etiqueta + ": ");
                return LocalDate.parse(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Formato de fecha inválido. Use yyyy-MM-dd (ej: 2025-01-15).");
            }
        }
    }
}
