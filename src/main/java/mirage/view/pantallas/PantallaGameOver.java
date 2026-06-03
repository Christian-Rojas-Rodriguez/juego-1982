// ============================================================
// PantallaGameOver — Pantalla de fin de partida
// ============================================================
// Qué implementar:
//   - render(sketch): mostrar "GAME OVER", puntaje final, vidas restantes
//                     instrucciones: R para reiniciar, ESC para volver al HOME
//   - update(): animación opcional (fade in, texto parpadeante, etc.)
// ============================================================

package mirage.view.pantallas;

import processing.core.PApplet;

public class PantallaGameOver implements Pantalla {

    // --- Atributos ---
    private final int puntajeFinal;
    private final int enemigosDerribados;

    // --- Constructor ---
    public PantallaGameOver(int puntajeFinal, int enemigosDerribados) {
        this.puntajeFinal       = puntajeFinal;
        this.enemigosDerribados = enemigosDerribados;
    }

    @Override
    public void render(PApplet sketch) {
        // TODO: sketch.fill(0, 0, 0, 180) → sketch.rect(0, 0, sketch.width, sketch.height) [fondo semitransparente]
        // TODO: texto "GAME OVER" centrado grande
        // TODO: mostrar puntajeFinal y enemigosDerribados
        // TODO: "R: Reiniciar | ESC: Volver al menú"
    }

    @Override
    public void update() {
        // TODO: animación opcional (ej: fade in del overlay)
    }
}
