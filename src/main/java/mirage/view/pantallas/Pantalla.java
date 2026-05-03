// ============================================================
// Pantalla — Interfaz para pantallas superpuestas al juego
// ============================================================
// GRASP : Polymorphism, Protected Variations
// ============================================================
// Implementaciones: PantallaJuego, PantallaGameOver
// El GameRenderer llama render(sketch) de la pantalla activa.
// ============================================================

package mirage.view.pantallas;

import processing.core.PApplet;

public interface Pantalla {

    void render(PApplet sketch);

    void update();
}
