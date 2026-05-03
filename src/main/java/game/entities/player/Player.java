package game.entities.player;

import game.entities.Nave;
import processing.core.PApplet;

public class Player extends Nave {

    private boolean izquierda, derecha, arriba, abajo;

    public Player(PApplet sketch) {
        super(sketch, sketch.width / 2f, sketch.height - 100, 4, 100);
    }

    @Override
    public void update() {
        if (izquierda) x -= velocidad;
        if (derecha)   x += velocidad;
        if (arriba)    y -= velocidad;
        if (abajo)     y += velocidad;

        x = PApplet.constrain(x, 15, sketch.width  - 15);
        y = PApplet.constrain(y, 20, sketch.height - 15);
    }

    @Override
    public void render() {
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