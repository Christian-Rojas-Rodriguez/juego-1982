package home;

/**
 * DTO inmutable del HOME team — estadísticas que el módulo le reporta al lobby.
 * No modificar: definido por el grupo Lobby (repo Modulo_1_Algoritmos_1).
 *
 * MirageModulo construye una instancia de este objeto en getEstadisticasGenerales()
 * mapeando los datos de EstadisticasMirage.
 */
public class EstadisticasGenerales {

    private final String nombreModulo;
    private final int    puntajeTotal;
    private final int    partidasJugadas;
    private final int    partidasGanadas;
    private final int    partidasPerdidas;
    private final int    enemigosDestruidos;
    private final double tiempoJugadoSegundos;

    public EstadisticasGenerales(String nombreModulo,
                                  int puntajeTotal,
                                  int partidasJugadas,
                                  int partidasGanadas,
                                  int partidasPerdidas,
                                  int enemigosDestruidos,
                                  double tiempoJugadoSegundos) {
        this.nombreModulo         = nombreModulo;
        this.puntajeTotal         = puntajeTotal;
        this.partidasJugadas      = partidasJugadas;
        this.partidasGanadas      = partidasGanadas;
        this.partidasPerdidas     = partidasPerdidas;
        this.enemigosDestruidos   = enemigosDestruidos;
        this.tiempoJugadoSegundos = tiempoJugadoSegundos;
    }

    public String getNombreModulo()         { return nombreModulo; }
    public int getPuntajeTotal()            { return puntajeTotal; }
    public int getPartidasJugadas()         { return partidasJugadas; }
    public int getPartidasGanadas()         { return partidasGanadas; }
    public int getPartidasPerdidas()        { return partidasPerdidas; }
    public int getEnemigosDestruidos()      { return enemigosDestruidos; }
    public double getTiempoJugadoSegundos() { return tiempoJugadoSegundos; }
}