package home;

/**
 * DTO del HOME team — evento que un módulo dispara a sus observers.
 * No modificar: definido por el grupo Lobby (repo Modulo_1_Algoritmos_1).
 *
 * MirageModulo crea instancias de este objeto al notificar cambios de estado.
 */
public class ModuloEvento {

    private final TipoEvento tipo;
    private final String nombreModulo;
    private final String mensaje;

    public ModuloEvento(TipoEvento tipo, String nombreModulo, String mensaje) {
        this.tipo         = tipo;
        this.nombreModulo = nombreModulo;
        this.mensaje      = mensaje;
    }

    public TipoEvento getTipo()       { return tipo; }
    public String getNombreModulo()   { return nombreModulo; }
    public String getMensaje()        { return mensaje; }
}