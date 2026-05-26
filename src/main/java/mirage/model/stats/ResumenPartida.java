package mirage.model.stats;

/**
 * DTO interno del módulo Mirage — snapshot de una partida finalizada.
 *
 * Creado por EstadisticasMirage.exportar() y usado por MirageModulo
 * para construir el EstadisticasGenerales que recibe el HOME team.
 *
 * Es inmutable: todos los campos se asignan en el constructor.
 */
public class ResumenPartida {

    private final int puntajeFinal;
    private final int enemigosDerribados;
    private final int vidasRestantes;
    private final float duracionSegundos;
    private final float precision;
    private final int partidasJugadas;
    private final int partidasGanadas;
    private final int partidasPerdidas;
    private final String moduloNombre = "Mirage - FAA";

    public ResumenPartida(int puntajeFinal,
                          int enemigosDerribados,
                          int vidasRestantes,
                          float duracionSegundos,
                          float precision,
                          int partidasJugadas,
                          int partidasGanadas,
                          int partidasPerdidas) {
        this.puntajeFinal      = puntajeFinal;
        this.enemigosDerribados = enemigosDerribados;
        this.vidasRestantes    = vidasRestantes;
        this.duracionSegundos  = duracionSegundos;
        this.precision         = precision;
        this.partidasJugadas   = partidasJugadas;
        this.partidasGanadas   = partidasGanadas;
        this.partidasPerdidas  = partidasPerdidas;
    }

    public int getPuntajeFinal()        { return puntajeFinal; }
    public int getEnemigosDerribados()  { return enemigosDerribados; }
    public int getVidasRestantes()      { return vidasRestantes; }
    public float getDuracionSegundos()  { return duracionSegundos; }
    public float getPrecision()         { return precision; }
    public int getPartidasJugadas()     { return partidasJugadas; }
    public int getPartidasGanadas()     { return partidasGanadas; }
    public int getPartidasPerdidas()    { return partidasPerdidas; }
    public String getModuloNombre()     { return moduloNombre; }
}
