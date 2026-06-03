package mirage.model.stats;

import mirage.model.entidades.Mirage;

import java.util.HashMap;
import java.util.Map;

/**
 * Recolecta métricas de la partida en tiempo real.
 *
 * ColisionDetector la llama para registrar aciertos y derribos.
 * Al finalizar la partida, exportar() construye un ResumenPartida interno
 * que ModuloMirage.getEstadisticasGenerales() mapea al DTO del HOME team.
 *
 * disparosTotales NO se almacena aquí: es responsabilidad de Mirage (quien dispara).
 * Para calcular precisión se consulta mirage.getDisparosTotales() en exportar().
 *
 * Patrón: GRASP Information Expert (quien registra sabe qué registrar)
 */
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
        // TODO: cargar()  — recuperar historial previo de archivo
        // TODO: tiempoInicioMs = System.currentTimeMillis()
    }

    // ── Registro durante la partida ──────────────────────────────────────────

    /**
     * Llamado por ColisionDetector cuando un proyectil del jugador impacta un enemigo.
     * Solo incrementa aciertos; el total de disparos está en Mirage.
     */
    public void registrarDisparoAcertado() {
        // TODO: disparosAcertados++
    }

    /**
     * Llamado por ColisionDetector cuando un enemigo queda con vida <= 0.
     *
     * @param tipo   identificador del tipo (ej: "HARRIER"), obtenido de enemigo.getTipo()
     * @param puntos puntos que otorgó el derribo
     */
    public void registrarDerribo(String tipo, int puntos) {
        // TODO: enemigosDerribados++
        // TODO: enemigosPorTipo.merge(tipo, 1, Integer::sum)
    }

    /** Llamado desde EstadoGameOver.alEntrar() al finalizar la partida. */
    public void registrarFinPartida(int puntajeFinal) {
        // TODO: float duracion = (System.currentTimeMillis() - tiempoInicioMs) / 1000f
        // TODO: partidasJugadas++
        // TODO: partidasPerdidas++  (en MVP 1 toda partida termina en derrota)
        // TODO: if (puntajeFinal > puntajeMaximo) puntajeMaximo = puntajeFinal
    }

    // ── Precisión ────────────────────────────────────────────────────────────

    /**
     * Calcula precisión de disparo.
     * Consulta mirage.getDisparosTotales() porque Mirage es dueño de ese dato.
     *
     * @return valor entre 0.0 y 1.0, o 0.0 si no se disparó
     */
    public float getPrecision(Mirage mirage) {
        // TODO: int totales = mirage.getDisparosTotales()
        // TODO: if (totales == 0) return 0f
        // TODO: return (float) disparosAcertados / totales
        return 0f;
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
        // TODO: float duracion = (System.currentTimeMillis() - tiempoInicioMs) / 1000f
        // TODO: return new ResumenPartida(
        //           mirage.getPuntuacion(),
        //           enemigosDerribados,
        //           vidasRestantes,
        //           duracion,
        //           getPrecision(mirage),
        //           partidasJugadas,
        //           partidasGanadas,
        //           partidasPerdidas
        //       )
        return null;
    }

    // ── Persistencia ────────────────────────────────────────────────────────

    public void guardar() {
        // TODO: escribir a "data/estadisticas_mirage.csv"
        // Columnas: fecha, puntaje, enemigosDerribados, precision, duracion
    }

    public void cargar() {
        // TODO: leer "data/estadisticas_mirage.csv" si existe
        // TODO: recuperar puntajeMaximo, partidasJugadas, etc.
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int getEnemigosDerribados()          { return enemigosDerribados; }
    public int getPuntajeMaximo()               { return puntajeMaximo; }
    public int getPartidasJugadas()             { return partidasJugadas; }
    public int getPartidasGanadas()             { return partidasGanadas; }
    public int getPartidasPerdidas()            { return partidasPerdidas; }
    public Map<String, Integer> getPorTipo()    { return enemigosPorTipo; }
}
