// ============================================================
// MirageModulo — Punto de entrada único del módulo al HOME
// ============================================================
// GRASP : Indirection, Low Coupling
// Patrón: Facade
// ============================================================
// Qué implementar:
//   - iniciar(sketch): crear GameController y arrancar el juego
//   - pausar() / reanudar(): delegar al estado activo del Controller
//   - getResumen(): pedirle al model las stats y devolver el DTO
// ============================================================

package mirage;

import processing.core.PApplet;
import mirage.controller.GameController;
import mirage.model.stats.ResumenPartida;

public class MirageModulo {

    // --- Atributos ---
    private GameController controller;

    // --- Iniciar módulo (llamado por el HOME) ---
    public void iniciar(PApplet sketch) {
        // TODO: new GameController(sketch) → controller.init()
    }

    // --- Control de pausa ---
    public void pausar() {
        // TODO: delegar al controller
    }

    public void reanudar() {
        // TODO: delegar al controller
    }

    // --- Game loop (delegado desde Juego1982.draw()) ---
    public void update() {
        // TODO: controller.update()
    }

    public void render() {
        // TODO: controller.render()
    }

    // --- Input (delegado desde Juego1982.keyPressed/Released) ---
    public void onKeyPressed(char key, int keyCode) {
        // TODO: controller.onKeyPressed(keyCode)
    }

    public void onKeyReleased(char key, int keyCode) {
        // TODO: controller.onKeyReleased(keyCode)
    }

    // --- Exportar resultado al HOME al terminar ---
    public ResumenPartida getResumen() {
        // TODO: controller.getEstadisticas().exportar()
        return null;
    }
}
