package mirage.view.pantallas;

import processing.core.PApplet;

/** Pantalla superpuesta al juego; el GameRenderer dibuja la pantalla activa. */
public interface Pantalla {

    void render(PApplet sketch);

    void update();
}
