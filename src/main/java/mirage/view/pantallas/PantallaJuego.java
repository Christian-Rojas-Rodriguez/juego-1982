package mirage.view.pantallas;

import mirage.view.sprites.SpriteLoader;
import processing.core.PApplet;
import processing.core.PImage;

public class PantallaJuego implements Pantalla {

    @Override
    public void render(PApplet sketch) {
        PImage fondo = SpriteLoader.get("fondo.png");
        if (fondo == null) {
            sketch.background(8, 22, 40);
            return;
        }
        float escala = Math.max(sketch.width / (float) fondo.width, sketch.height / (float) fondo.height);
        float ancho  = fondo.width  * escala;
        float alto   = fondo.height * escala;
        float x      = (sketch.width  - ancho) / 2f;
        float y      = (sketch.height - alto)  / 2f;
        sketch.imageMode(PApplet.CORNER);
        sketch.image(fondo, x, y, ancho, alto);
    }

}
