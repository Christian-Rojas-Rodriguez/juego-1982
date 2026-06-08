package mirage.model.stats;

import mirage.model.entidades.Mirage;

import java.util.HashMap;
import java.util.Map;

public class EstadisticasMirage {

    // ── Métricas de la partida actual ────────────────────────────────────────
    private int disparosAcertados;
    private int enemigosDerribados;
    private final Map<String, Integer> enemigosPorTipo = new HashMap<>();
    private long tiempoInicioMs;

    // ── Historial persistido (acumulado entre partidas) ───────────────────────
    private int puntajeMaximo;
    private int partidasJugadas;
    private int partidasGanadas;  // reservado para MVPs con condición de victoria
    private int partidasPerdidas;

    public EstadisticasMirage() {
        this.tiempoInicioMs = System.currentTimeMillis();
        cargar();  // no-op en v1 (persistencia CSV fuera de v1)
    }

    // ── Registro durante la partida ──────────────────────────────────────────

    /**
     * Llamado por ColisionDetector cuando un proyectil del jugador impacta un enemigo.
     * Solo incrementa aciertos; el total de disparos está en Mirage.
     */
    public void registrarDisparoAcertado() {
        disparosAcertados++;
    }

    /**
     * Llamado por ColisionDetector cuando un enemigo queda con vida <= 0.
     *
     * @param tipo   identificador del tipo (ej: "HARRIER"), obtenido de enemigo.getTipo()
     * @param puntos puntos que otorgó el derribo
     */
    public void registrarDerribo(String tipo, int puntos) {
        enemigosDerribados++;
        enemigosPorTipo.merge(tipo, 1, Integer::sum);
    }

    /** Llamado desde EstadoGameOver.alEntrar() al finalizar la partida. */
    public void registrarFinPartida(int puntajeFinal) {
        partidasJugadas++;
        partidasPerdidas++;  // en v1 toda partida termina en derrota
        if (puntajeFinal > puntajeMaximo) puntajeMaximo = puntajeFinal;
    }

    // ── Precisión ────────────────────────────────────────────────────────────

    /**
     * Calcula precisión de disparo.
     * Consulta mirage.getDisparosTotales() porque Mirage es dueño de ese dato.
     *
     * @return valor entre 0.0 y 1.0, o 0.0 si no se disparó
     */
    public float getPrecision(Mirage mirage) {
        int totales = mirage.getDisparosTotales();
        if (totales == 0) return 0f;
        return (float) disparosAcertados / totales;
    }

    // ── Export al HOME ───────────────────────────────────────────────────────

    /**
     * Construye el ResumenPartida con todos los datos de la partida.
     * ModuloMirage.getEstadisticasGenerales() lo usa para crear el DTO del HOME.
     *
     * @param vidasRestantes vidas que le quedaban al Mirage al terminar
     * @param mirage         referencia para obtener disparosTotales y puntuacion
     */
    public ResumenPartida exportar(int vidasRestantes, Mirage mirage) {
        float duracion = (System.currentTimeMillis() - tiempoInicioMs) / 1000f;
        return new ResumenPartida(
                mirage.getPuntuacion(),
                enemigosDerribados,
                vidasRestantes,
                duracion,
                getPrecision(mirage),
                partidasJugadas,
                partidasGanadas,
                partidasPerdidas
        );
    }

    // ── Persistencia ────────────────────────────────────────────────────────

    public void cargar() {
        // No-op en v1: persistencia CSV fuera de v1 (no se leen archivos).
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int getEnemigosDerribados()          { return enemigosDerribados; }
    public int getPartidasJugadas()             { return partidasJugadas; }
    public int getPartidasGanadas()             { return partidasGanadas; }
    public int getPartidasPerdidas()            { return partidasPerdidas; }
}
