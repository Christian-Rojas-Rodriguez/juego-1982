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
        // Overlay semitransparente sobre el juego de fondo.
        sketch.fill(0, 0, 0, 180);
        sketch.rectMode(PApplet.CORNER);
        sketch.rect(0, 0, sketch.width, sketch.height);

        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);

        sketch.fill(220, 50, 50);
        sketch.textSize(40);
        sketch.text("GAME OVER", sketch.width / 2f, sketch.height * 0.35f);

        sketch.fill(255);
        sketch.textSize(16);
        sketch.text("Puntaje: " + puntajeFinal, sketch.width / 2f, sketch.height * 0.52f);
        sketch.text("Enemigos derribados: " + enemigosDerribados,
                    sketch.width / 2f, sketch.height * 0.60f);

        sketch.fill(180);
        sketch.textSize(11);
        sketch.text("R: Reiniciar   |   ESC: Volver al menu",
                    sketch.width / 2f, sketch.height * 0.75f);
    }

    @Override
    public void update() {
        // TODO: animación opcional (ej: fade in del overlay)
    }
}
