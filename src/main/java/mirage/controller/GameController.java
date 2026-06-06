package mirage.controller;

import mirage.ModuloMirage;
import mirage.controller.commands.DispararCmd;
import mirage.controller.commands.MoverAbajoCmd;
import mirage.controller.commands.MoverArribaCmd;
import mirage.controller.commands.MoverDerechaCmd;
import mirage.controller.commands.MoverIzquierdaCmd;
import mirage.model.estado.EstadoJuego;
import mirage.model.estado.EstadoJugando;
import mirage.model.entidades.Mirage;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.fisica.ColisionDetector;
import mirage.model.niveles.NivelMirage;
import mirage.model.stats.EstadisticasMirage;
import mirage.view.GameRenderer;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Orquestador central del módulo Mirage.
 *
 * Recibe todos los eventos de Processing (via ModuloMirage) y coordina
 * Model y View. Aplica el patrón State: delega update/render al estado activo.
 *
 * Los proyectiles son propiedad de Mirage; GameController los consulta
 * via mirage.getProyectiles() para colisionar y limpiar.
 *
 * Patrón: GRASP Controller + State
 */
public class GameController {

    private final PApplet sketch;
    private final ModuloMirage mirageModulo;

    private EstadoJuego estadoActual;
    private Mirage mirage;
    private List<Enemigo> enemigos;

    private NivelMirage nivel;
    private ColisionDetector colisionDetector;
    private EstadisticasMirage estadisticas;
    private GameRenderer renderer;
    private InputHandler inputHandler;

    public GameController(PApplet sketch, ModuloMirage mirageModulo) {
        this.sketch        = sketch;
        this.mirageModulo  = mirageModulo;
    }

    /**
     * Crea e inicializa todas las dependencias.
     * Llamado desde ModuloMirage.inicializarContexto() y reset().
     */
    public void init() {
        mirage           = new Mirage(sketch);
        enemigos         = new ArrayList<>();
        estadisticas     = new EstadisticasMirage();
        colisionDetector = new ColisionDetector(estadisticas);
        nivel            = new NivelMirage(sketch);
        renderer         = new GameRenderer();
        inputHandler     = new InputHandler();

        inputHandler.registrarComando(PApplet.LEFT,  new MoverIzquierdaCmd());
        inputHandler.registrarComando(PApplet.RIGHT, new MoverDerechaCmd());
        inputHandler.registrarComando(PApplet.UP,    new MoverArribaCmd());
        inputHandler.registrarComando(PApplet.DOWN,  new MoverAbajoCmd());
        inputHandler.registrarComando(32,            new DispararCmd());  // 32 = SPACE

        setEstado(new EstadoJugando());
    }

    // ── Game loop ────────────────────────────────────────────────────────────

    public void update() {
        estadoActual.update(this);
    }

    public void render() {
        estadoActual.render(this);
    }

    // ── Estado ───────────────────────────────────────────────────────────────

    /**
     * Cambia el estado activo y llama alEntrar() en el nuevo estado.
     * Usado por los estados y por ModuloMirage para transicionar.
     */
    public void setEstado(EstadoJuego nuevoEstado) {
        this.estadoActual = nuevoEstado;
        nuevoEstado.alEntrar(this);
    }

    // ── Input ────────────────────────────────────────────────────────────────

    public void onKeyPressed(char key, int keyCode) {
        estadoActual.onKeyPressed(this, key, keyCode);   // state handles P, R, ESC
        inputHandler.onKeyPressed(keyCode, key, mirage); // command handles movement + SPACE
    }

    public void onKeyReleased(char key, int keyCode) {
        inputHandler.onKeyReleased(keyCode, key, mirage);
    }

    // ── Getters (solo lectura para los estados) ───────────────────────────────

    public PApplet getSketch()                    { return sketch; }
    public ModuloMirage getMirageModulo()          { return mirageModulo; }
    public Mirage getMirage()                     { return mirage; }
    public List<Enemigo> getEnemigos()            { return enemigos; }
    public NivelMirage getNivel()                 { return nivel; }
    public ColisionDetector getColisionDetector() { return colisionDetector; }
    public EstadisticasMirage getEstadisticas()   { return estadisticas; }
    public GameRenderer getRenderer()             { return renderer; }
}