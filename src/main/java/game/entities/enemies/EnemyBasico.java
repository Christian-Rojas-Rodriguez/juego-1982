package game.entities.enemies;

import processing.core.PApplet;

public class EnemyBasico extends Enemy {

    public EnemyBasico(PApplet sketch, float x, float y) {
        super(sketch, x, y, 4f, 30, 100);
    }

    @Override
    protected void moverIA() {
        x += velocidad;
        y += 2f;

        if (x <= 15 || x >= sketch.width - 15) {
            velocidad *= -1;
            x = PApplet.constrain(x, 15, sketch.width - 15);
        }
    }

    @Override
    public void render() {
        sketch.noStroke();
        sketch.fill(255, 50, 50);
        sketch.triangle(x, y + 20, x - 15, y - 15, x + 15, y - 15);
    }
}