package home;

/**
 * Interfaz del HOME team — contrato que todo módulo de avión debe implementar.
 * No modificar: definido por el grupo Lobby (repo Modulo_1_Algoritmos_1).
 *
 * MirageModulo implementa esta interfaz. El HOME team registra el módulo con:
 *   homeJuego.registrarModulo(new MirageModulo())
 * y luego controla su ciclo de vida llamando a estos métodos.
 */
public interface ModuloJuego {

    /** Configuración inicial: cargar sprites, crear dependencias. */
    void inicializarContexto(processing.core.PApplet sketch);

    /** Comienza la partida. Dispara ModuloEvento(INICIADO). */
    void iniciar();

    /** Congela la lógica del juego. Dispara ModuloEvento(PAUSADO). */
    void pausar();

    /** Reanuda la lógica del juego. Dispara ModuloEvento(REANUDADO). */
    void reanudar();

    /** Finaliza el módulo (volver al lobby). Dispara ModuloEvento(FINALIZADO). */
    void finalizar();

    /** Llamado en cada draw() de Processing — actualiza la lógica. */
    void actualizar();

    /** Llamado en cada draw() de Processing — renderiza el estado actual. */
    void dibujar();

    /** Retorna las estadísticas para el lobby (score, tiempo, enemigos). */
    EstadisticasGenerales getEstadisticasGenerales();

    /** Registra un observer. El HOME lo usa para recibir ModuloEvento. */
    void agregarObserver(IModuloObserver observer);

    /** Quita un observer previamente registrado. */
    void removerObserver(IModuloObserver observer);

    /** Reinicia la partida desde cero manteniendo las estadísticas históricas. */
    void reset();
}