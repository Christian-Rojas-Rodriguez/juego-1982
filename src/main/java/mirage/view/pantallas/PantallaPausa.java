package mirage.view.pantallas;

import processing.core.PApplet;

public class PantallaPausa implements Pantalla {

    @Override
    public void render(PApplet sketch) {
        // Overlay semitransparente sobre el juego congelado de fondo.
        sketch.fill(0, 0, 0, 140);
        sketch.rectMode(PApplet.CORNER);
        sketch.rect(0, 0, sketch.width, sketch.height);

        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);

        sketch.fill(255, 220, 0);
        sketch.textSize(28);
        sketch.text("PAUSA", sketch.width / 2f, sketch.height / 2f);

        sketch.fill(180);
        sketch.textSize(11);
        sketch.text("P: Reanudar", sketch.width / 2f, sketch.height * 0.62f);
    }
}
