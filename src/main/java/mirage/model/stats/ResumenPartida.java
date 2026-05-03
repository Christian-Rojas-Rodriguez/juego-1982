// ============================================================
// ResumenPartida — DTO exportado al HOME al terminar la partida
// ============================================================
// GRASP : Low Coupling (objeto de transferencia sin dependencias)
// ============================================================
// Este es el "contrato de datos" entre el módulo Mirage y el HOME.
// Solo campos y getters — sin lógica de negocio.
// El HOME puede mostrar estos datos en la pantalla de estadísticas general.
// ============================================================

package mirage.model.stats;

public class ResumenPartida {

    // --- Atributos ---
    private final int puntajeFinal;
    private final int enemigosDerribados;
    private final int vidasRestantes;
    private final float duracionSegundos;
    private final String moduloNombre = "Mirage - FAA";

    // --- Constructor ---
    public ResumenPartida(int puntajeFinal, int enemigosDerribados, int vidasRestantes, float duracionSegundos) {
        // TODO: asignar todos los parámetros
    }

    // --- Getters ---
    public int getPuntajeFinal()         { return puntajeFinal; }
    public int getEnemigosDerribados()   { return enemigosDerribados; }
    public int getVidasRestantes()       { return vidasRestantes; }
    public float getDuracionSegundos()   { return duracionSegundos; }
    public String getModuloNombre()      { return moduloNombre; }
}
