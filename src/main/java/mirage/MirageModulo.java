package mirage;

import home.EstadisticasGenerales;
import home.IModuloObserver;
import home.ModuloEvento;
import home.ModuloJuego;
import home.TipoEvento;
import mirage.controller.GameController;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Facade del módulo Mirage hacia el HOME team.
 *
 * Implementa ModuloJuego (contrato del HOME). Es el único punto de contacto
 * entre el lobby y toda la lógica interna del módulo.
 *
 * Responsabilidades:
 *   - Gestionar el ciclo de vida (iniciar, pausar, reanudar, finalizar, reset)
 *   - Delegar el game loop a GameController (actualizar / dibujar)
 *   - Mantener la lista de observers y notificarles cambios de estado
 *   - Mapear EstadisticasMirage → EstadisticasGenerales para el lobby
 *
 * Patrón: Facade + Observer (como sujeto)
 */
public class MirageModulo implements ModuloJuego {

    private static final String NOMBRE_MODULO = "Mirage - FAA";

    private GameController controller;
    private PApplet sketch;

    /** Observers registrados por el HOME (normalmente solo HomeJuego). */
    private final List<IModuloObserver> observers = new ArrayList<>();

    // ── Ciclo de vida ────────────────────────────────────────────────────────

    @Override
    public void inicializarContexto(PApplet sketch) {
        this.sketch = sketch;
        // TODO: controller = new GameController(sketch, this)
        // TODO: controller.init()
    }

    @Override
    public void iniciar() {
        // TODO: controller.setEstado(new EstadoJugando())
        // TODO: notificar(TipoEvento.INICIADO)
    }

    @Override
    public void pausar() {
        // TODO: controller.setEstado(new EstadoPausado())
        // TODO: notificar(TipoEvento.PAUSADO)
    }

    @Override
    public void reanudar() {
        // TODO: controller.setEstado(new EstadoJugando())
        // TODO: notificar(TipoEvento.REANUDADO)
    }

    @Override
    public void finalizar() {
        // TODO: notificar(TipoEvento.FINALIZADO)
    }

    @Override
    public void reset() {
        // TODO: controller.init()  — recrea todo desde cero
        // TODO: notificar(TipoEvento.INICIADO)
    }

    // ── Game loop ────────────────────────────────────────────────────────────

    @Override
    public void actualizar() {
        // TODO: controller.update()
    }

    @Override
    public void dibujar() {
        // TODO: controller.render()
    }

    // ── Input (no forma parte de ModuloJuego; usado en modo standalone) ──────

    public void onKeyPressed(char key, int keyCode) {
        // TODO: controller.onKeyPressed(key, keyCode)
    }

    public void onKeyReleased(char key, int keyCode) {
        // TODO: controller.onKeyReleased(key, keyCode)
    }

    // ── Estadísticas para el HOME ────────────────────────────────────────────

    @Override
    public EstadisticasGenerales getEstadisticasGenerales() {
        // TODO: ResumenPartida r = controller.getEstadisticas()
        //                                    .exportar(controller.getMirage().getVidas(),
        //                                              controller.getMirage())
        // TODO: return new EstadisticasGenerales(
        //           NOMBRE_MODULO,
        //           r.getPuntajeFinal(),
        //           r.getPartidasJugadas(),
        //           r.getPartidasGanadas(),
        //           r.getPartidasPerdidas(),
        //           r.getEnemigosDerribados(),
        //           r.getDuracionSegundos()
        //       )
        return null;
    }

    // ── Observer ─────────────────────────────────────────────────────────────

    @Override
    public void agregarObserver(IModuloObserver observer) {
        // TODO: observers.add(observer)
    }

    @Override
    public void removerObserver(IModuloObserver observer) {
        // TODO: observers.remove(observer)
    }

    /** Notifica a todos los observers registrados con el evento dado. */
    void notificar(TipoEvento tipo) {
        // TODO: ModuloEvento evento = new ModuloEvento(tipo, NOMBRE_MODULO, "")
        // TODO: for (IModuloObserver o : observers) o.onEventoModulo(evento)
    }
}