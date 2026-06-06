package mirage;

import contracts.ContextoJuego;
import contracts.EnEjecucionState;
import contracts.EstadisticasGenerales;
import contracts.EstadoInvalidoException;
import contracts.EstadoJuego;
import contracts.FinalizadoState;
import contracts.IModuloObserver;
import contracts.IniciandoState;
import contracts.ModuloEvento;
import contracts.ModuloJuego;
import contracts.NoIniciadoState;
import contracts.PausadoState;
import mirage.controller.GameController;
import mirage.model.stats.ResumenPartida;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Módulo Mirage: implementación del contrato real del HOME (contracts.ModuloJuego).
 *
 * Es el único punto de contacto entre el lobby y la lógica interna del módulo.
 * Gestiona el ciclo de vida con la máquina de estados del HOME
 * (NO_INICIADO → INICIANDO → EN_EJECUCION ↔ PAUSADO → FINALIZADO),
 * notifica eventos a los observers y devuelve estadísticas no nulas.
 *
 * Estado actual: integración funcional con gameplay PLACEHOLDER. La máquina de
 * estados, observers y stats están completos y se ejercitan con el lobby; la
 * lógica de juego real (GameController + entidades) se cablea donde están los TODO.
 *
 * Patrón: Facade (oculta el interior) + State (ciclo de vida) + Observer (sujeto).
 */
public class ModuloMirage implements ModuloJuego {

    private static final String NOMBRE_MODULO = "Mirage";
    private static final String DESCRIPCION   =
            "Shooter aereo vertical inspirado en 1982. Pilota un Dassault Mirage III.";
    private static final String NOMBRE_AVION  = "Dassault Mirage III";

    /** Duración (ms) de la fase de carga simulada antes de jugar. */
    private static final int CARGA_MS = 600;

    /** Estado del ciclo de vida del HOME (no confundir con mirage.model.estado). */
    private EstadoJuego estadoActual;

    /** Observers registrados por el HOME (normalmente solo HomeJuego). */
    private final List<IModuloObserver> observers = new ArrayList<>();

    /** Contexto provisto por el lobby (jugador + dimensiones de pantalla). */
    private ContextoJuego contexto;

    /** Game loop real (lazy-init en actualizar(), donde llega el PApplet vivo). */
    private GameController controller;

    // ── Marcas de tiempo / métricas placeholder ───────────────────────────────
    private long tInicioCargaMs;
    private long tInicioJuegoMs;
    private long tAcumuladoMs;       // tiempo jugado acumulado (para stats)
    private int  puntaje;

    public ModuloMirage() {
        this.estadoActual = new NoIniciadoState();
    }

    // ── Identidad ─────────────────────────────────────────────────────────────

    @Override
    public String getNombreModulo() { return NOMBRE_MODULO; }

    @Override
    public String getDescripcion()  { return DESCRIPCION; }

    @Override
    public String getNombreAvion()  { return NOMBRE_AVION; }

    // ── Inicialización ────────────────────────────────────────────────────────

    @Override
    public void inicializarContexto(ContextoJuego ctx) {
        this.contexto = ctx;
        // El PApplet vivo llega recién en actualizar/dibujar; el GameController se
        // crea perezosamente ahí. Las dimensiones están en ctx.getAncho/AltoPantalla().
        // TODO: precargar recursos que no necesiten PApplet.
    }

    // ── Ciclo de vida (valida en el estado actual, luego transiciona) ─────────

    @Override
    public void iniciar() throws EstadoInvalidoException {
        estadoActual.iniciar(this);                 // lanza si la transición es inválida
        estadoActual = new IniciandoState();
        tInicioCargaMs = System.currentTimeMillis();
        puntaje = 0;
        tAcumuladoMs = 0;
        notificar(ModuloEvento.Tipo.INICIADO, "Cargando modulo Mirage...");
    }

    @Override
    public void pausar() throws EstadoInvalidoException {
        estadoActual.pausar(this);
        estadoActual = new PausadoState();
        tAcumuladoMs += System.currentTimeMillis() - tInicioJuegoMs;
        notificar(ModuloEvento.Tipo.PAUSADO, "Juego en pausa");
    }

    @Override
    public void reanudar() throws EstadoInvalidoException {
        estadoActual.reanudar(this);
        estadoActual = new EnEjecucionState();
        tInicioJuegoMs = System.currentTimeMillis();
        notificar(ModuloEvento.Tipo.REANUDADO, "Juego reanudado");
    }

    @Override
    public void finalizar() throws EstadoInvalidoException {
        estadoActual.finalizar(this);
        if ("EN_EJECUCION".equals(estadoActual.getNombre())) {
            tAcumuladoMs += System.currentTimeMillis() - tInicioJuegoMs;
        }
        estadoActual = new FinalizadoState();
        notificar(ModuloEvento.Tipo.FINALIZADO, "Juego finalizado");
    }

    // ── Game loop ─────────────────────────────────────────────────────────────

    @Override
    public void actualizar(PApplet app) {
        // Lazy-init headless-safe: solo crea el controller (no toca graphics).
        if (controller == null) {
            controller = new GameController(app, this);
            controller.init();
        }
        String estado = estadoActual.getNombre();

        if ("INICIANDO".equals(estado)) {
            // Fin de la carga simulada → pasar a ejecución.
            if (System.currentTimeMillis() - tInicioCargaMs >= CARGA_MS) {
                estadoActual = new EnEjecucionState();
                tInicioJuegoMs = System.currentTimeMillis();
                notificar(ModuloEvento.Tipo.INICIADO, "En ejecucion");
            }
        } else if ("EN_EJECUCION".equals(estado)) {
            controller.update();  // lógica de juego real (no llama graphics)
            // Placeholder: el puntaje crece con el tiempo jugado.
            long ms = tAcumuladoMs + (System.currentTimeMillis() - tInicioJuegoMs);
            puntaje = (int) (ms / 100);
            // TODO: si fin de partida (vidas == 0) → finalizar() automáticamente.
        }
        // PAUSADO/FINALIZADO: no se actualiza lógica.
    }

    @Override
    public void dibujar(PApplet app) {
        String estado = estadoActual.getNombre();

        // Gameplay real: delega el render al GameController (usa el PApplet vivo).
        if ("EN_EJECUCION".equals(estado) && controller != null) {
            controller.render();
            return;
        }

        app.background(8, 12, 24);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);

        app.fill(120, 200, 255);
        app.textSize(18);
        app.text("MIRAGE", app.width / 2f, app.height * 0.18f);
        app.fill(90, 140, 180);
        app.textSize(8);
        app.text(NOMBRE_AVION, app.width / 2f, app.height * 0.25f);

        app.fill(0, 200, 120);
        app.textSize(9);
        app.text("ESTADO: " + estado, app.width / 2f, app.height * 0.34f);

        if ("INICIANDO".equals(estado)) {
            float p = PApplet.constrain(
                    (System.currentTimeMillis() - tInicioCargaMs) / (float) CARGA_MS, 0, 1);
            app.fill(120, 200, 255);
            app.textSize(9);
            app.text("CARGANDO... " + (int) (p * 100) + "%", app.width / 2f, app.height * 0.5f);
        } else if ("PAUSADO".equals(estado)) {
            app.fill(255, 220, 0);
            app.textSize(16);
            app.text("-- PAUSA --", app.width / 2f, app.height * 0.5f);
            app.fill(90, 140, 180);
            app.textSize(8);
            app.text("ESC: reanudar   |   Q: finalizar", app.width / 2f, app.height * 0.62f);
        }
    }

    @Override
    public EstadoJuego getEstado() {
        return estadoActual;
    }

    // ── Estadísticas para el HOME (nunca null) ────────────────────────────────

    @Override
    public EstadisticasGenerales getEstadisticasGenerales() {
        if (controller != null && controller.getMirage() != null) {
            ResumenPartida r = controller.getEstadisticas()
                    .exportar(controller.getMirage().getVidas(), controller.getMirage());
            return new EstadisticasGenerales(
                    NOMBRE_MODULO,
                    r.getPuntajeFinal(),
                    r.getPartidasJugadas(),
                    r.getPartidasGanadas(),
                    r.getPartidasPerdidas(),
                    r.getEnemigosDerribados(),
                    (long) r.getDuracionSegundos()
            );
        }
        // Fallback (aún no se creó el game loop): DTO no-null basado en el placeholder.
        long enJuegoMs = tAcumuladoMs;
        if ("EN_EJECUCION".equals(estadoActual.getNombre())) {
            enJuegoMs += System.currentTimeMillis() - tInicioJuegoMs;
        }
        return new EstadisticasGenerales(NOMBRE_MODULO, puntaje, 1, 0, 1, 0, enJuegoMs / 1000);
    }

    // ── Observer ──────────────────────────────────────────────────────────────

    @Override
    public void agregarObserver(IModuloObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removerObserver(IModuloObserver observer) {
        observers.remove(observer);
    }

    /** Notifica a una copia de la lista (evita ConcurrentModification si un observer se desregistra). */
    private void notificar(ModuloEvento.Tipo tipo, String mensaje) {
        ModuloEvento evento = new ModuloEvento(tipo, NOMBRE_MODULO, mensaje);
        for (IModuloObserver o : new ArrayList<>(observers)) {
            o.onEventoModulo(evento);
        }
    }

    // ── Reset (para volver a jugar) ───────────────────────────────────────────

    @Override
    public void reset() {
        estadoActual = new NoIniciadoState();
        puntaje = 0;
        tAcumuladoMs = 0;
        if (controller != null) controller.init();  // recrea entidades/nivel desde cero
        observers.clear();   // el lobby se re-registra en seleccionarModulo()
    }

    // ── Input (NO forma parte del contrato HOME) ──────────────────────────────
    // El HOME intercepta ESC (pausar/reanudar) y Q (finalizar); el módulo NO los
    // maneja. Estos métodos existen SOLO para el runner standalone Juego1982.

    public void onKeyPressed(char key, int keyCode) {
        if (controller != null) controller.onKeyPressed(key, keyCode);
    }

    public void onKeyReleased(char key, int keyCode) {
        if (controller != null) controller.onKeyReleased(key, keyCode);
    }
}
