// ============================================================
// EstadisticasMirage — Estadísticas propias del módulo Mirage
// ============================================================
// GRASP : Information Expert, High Cohesion
// ============================================================
// Qué implementar:
//   - registrarDerribo(puntos): acumular enemigos destruidos y puntos
//   - registrarFinPartida(puntaje): guardar si es puntaje máximo
//   - guardar(): persistir en archivo CSV o JSON (ver java.io.FileWriter)
//     Ruta sugerida: "data/estadisticas_mirage.csv"
//   - cargar(): leer el archivo al iniciar para recuperar historial
//   - exportar(): construir y retornar un ResumenPartida (DTO para el HOME)
// ============================================================

package mirage.model.stats;

public class EstadisticasMirage {

    // --- Atributos (partida actual) ---
    private int enemigosDerribados;
    private float tiempoInicioMs;
    private float duracionSegundos;

    // --- Atributos (historial persistido) ---
    private int puntajeMaximo;
    private int partidasJugadas;

    // --- Constructor ---
    public EstadisticasMirage() {
        // TODO: cargar() para recuperar historial previo
        // TODO: tiempoInicioMs = System.currentTimeMillis()
    }

    // --- Registrar eventos durante la partida ---
    public void registrarDerribo(int puntos) {
        // TODO: enemigosDerribados++
    }

    public void registrarFinPartida(int puntajeFinal) {
        // TODO: duracionSegundos = (System.currentTimeMillis() - tiempoInicioMs) / 1000f
        // TODO: partidasJugadas++
        // TODO: si puntajeFinal > puntajeMaximo → puntajeMaximo = puntajeFinal
    }

    // --- Persistencia ---
    public void guardar() {
        // TODO: escribir a "data/estadisticas_mirage.csv"
        // Columnas sugeridas: fecha, puntaje, enemigosDerribados, duracion
    }

    public void cargar() {
        // TODO: leer "data/estadisticas_mirage.csv" si existe
        // TODO: recuperar puntajeMaximo y partidasJugadas
    }

    // --- Exportar al HOME ---
    public ResumenPartida exportar() {
        // TODO: return new ResumenPartida(puntaje, enemigosDerribados, vidasRestantes, duracion)
        return null;
    }

    // --- Getters ---
    public int getEnemigosDerribados() { return enemigosDerribados; }
    public int getPuntajeMaximo()      { return puntajeMaximo; }
    public int getPartidasJugadas()    { return partidasJugadas; }
}
