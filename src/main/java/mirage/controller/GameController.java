// ============================================================
// GameController — Orquestador principal del módulo Mirage
// ============================================================
// GRASP : Controller, High Cohesion
// Patrón: State (delega update/render al EstadoJuego activo)
// ============================================================
// Qué implementar:
//   - init(): crear todas las dependencias (Mirage, listas, nivel, renderer)
//   - update(): delegar al estadoActual
//   - render(): delegar al renderer pasando el model actual
//   - setEstado(): cambiar estado y llamar alEntrar()
//   - onKeyPressed/Released(): delegar al inputHandler
//   - Getters para que los estados puedan acceder al model
// ============================================================

package mirage.controller;

import processing.core.PApplet;
import mirage.model.estado.EstadoJuego;
import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.fisica.ColisionDetector;
import mirage.model.niveles.NivelMirage;
import mirage.model.stats.EstadisticasMirage;
import mirage.view.GameRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    // --- Atributos ---
    private final PApplet sketch;
    private EstadoJuego estadoActual;

    private Mirage mirage;
    private List<Enemigo> enemigos;
    private List<Proyectil> proyectiles;

    private NivelMirage nivel;
    private ColisionDetector colisionDetector;
    private EstadisticasMirage estadisticas;
    private GameRenderer renderer;
    private InputHandler inputHandler;

    // --- Constructor ---
    public GameController(PApplet sketch) {
        this.sketch = sketch;
    }

    // --- Inicialización ---
    public void init() {
        // TODO: instanciar mirage, enemigos (new ArrayList), proyectiles (new ArrayList)
        // TODO: instanciar nivel, colisionDetector, estadisticas
        // TODO: instanciar renderer, inputHandler
        // TODO: setEstado(new EstadoJugando())
    }

    // --- Game loop ---
    public void update() {
        // TODO: estadoActual.update(this)
    }

    public void render() {
        // TODO: estadoActual.render(this)
    }

    // --- Cambio de estado (patrón State) ---
    public void setEstado(EstadoJuego nuevoEstado) {
        // TODO: estadoActual = nuevoEstado → nuevoEstado.alEntrar(this)
    }

    // --- Input ---
    public void onKeyPressed(int keyCode) {
        // TODO: inputHandler.onKeyPressed(keyCode, mirage)
    }

    public void onKeyReleased(int keyCode) {
        // TODO: inputHandler.onKeyReleased(keyCode)
    }

    // --- Getters (solo lectura para los estados) ---
    public PApplet getSketch()                    { return sketch; }
    public Mirage getMirage()                     { return mirage; }
    public List<Enemigo> getEnemigos()            { return enemigos; }
    public List<Proyectil> getProyectiles()       { return proyectiles; }
    public NivelMirage getNivel()                 { return nivel; }
    public ColisionDetector getColisionDetector() { return colisionDetector; }
    public EstadisticasMirage getEstadisticas()   { return estadisticas; }
    public GameRenderer getRenderer()             { return renderer; }
}
