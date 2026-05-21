package game.entities.player;

import game.entities.Nave;
import processing.core.PApplet;
import processing.core.PImage;

public class Player extends Nave {

    private static final String SPRITE_PATH = "assets/images/mirage/mirage.png";
    private static final float SPRITE_ANCHO = 60;
    private static final float SPRITE_ALTO = 60;

    private boolean izquierda, derecha, arriba, abajo;
    private final PImage sprite;

    public Player(PApplet sketch) {
        super(sketch, sketch.width / 2f, sketch.height - 100, 4, 100);
        sprite = sketch.loadImage(SPRITE_PATH);
    }

    @Override
    public void update() {
        if (izquierda) x -= velocidad;
        if (derecha)   x += velocidad;
        if (arriba)    y -= velocidad;
        if (abajo)     y += velocidad;

        x = PApplet.constrain(x, SPRITE_ANCHO / 2, sketch.width  - SPRITE_ANCHO / 2);
        y = PApplet.constrain(y, SPRITE_ALTO / 2, sketch.height - SPRITE_ALTO / 2);
    }

    @Override
    public void render() {
        if (sprite != null) {
            sketch.imageMode(PApplet.CENTER);
            sketch.image(sprite, x, y, SPRITE_ANCHO, SPRITE_ALTO);
            return;
        }

        sketch.noStroke();
        sketch.fill(0, 200, 255);
        sketch.triangle(x, y - 20, x - 15, y + 15, x + 15, y + 15);
    }

    public void onKeyPressed(int keyCode) {
        if (keyCode == PApplet.LEFT)  izquierda = true;
        if (keyCode == PApplet.RIGHT) derecha   = true;
        if (keyCode == PApplet.UP)    arriba    = true;
        if (keyCode == PApplet.DOWN)  abajo     = true;
    }

    public void onKeyReleased(int keyCode) {
        if (keyCode == PApplet.LEFT)  izquierda = false;
        if (keyCode == PApplet.RIGHT) derecha   = false;
        if (keyCode == PApplet.UP)    arriba    = false;
        if (keyCode == PApplet.DOWN)  abajo     = false;
    }
}
