package mirage.model.efectos;

import processing.core.PApplet;

public class Explosion {

    /** Vida total del efecto, en frames (~1/3 seg a 60 fps). */
    private static final int DURACION = 22;
    private static final int N_PARTICULAS = 8;

    private final float x;
    private final float y;
    private int vida;   // frames restantes (DURACION → 0)

    public Explosion(float x, float y) {
        this.x    = x;
        this.y    = y;
        this.vida = DURACION;
    }

    public void update() {
        if (vida > 0) vida--;
    }

    public boolean terminada() {
        return vida <= 0;
    }

    public void render(PApplet sk) {
        float t     = (DURACION - vida) / (float) DURACION; // 0 → 1 (avance)
        float alpha = PApplet.lerp(255, 0, t);              // se desvanece

        sk.pushStyle();
        sk.noStroke();
        sk.rectMode(PApplet.CENTER);

        // Destello central: arranca grande y se encoge.
        float flash = PApplet.lerp(20, 4, t);
        sk.fill(255, 240, 180, alpha);
        sk.rect(x, y, flash, flash);

        // Partículas cuadradas que salen disparadas en círculo.
        float dist = PApplet.lerp(2, 26, t);
        float tam  = PApplet.lerp(7, 2, t);
        for (int k = 0; k < N_PARTICULAS; k++) {
            float ang = (float) (k * PApplet.TWO_PI / N_PARTICULAS);
            float px  = x + (float) Math.cos(ang) * dist;
            float py  = y + (float) Math.sin(ang) * dist;
            // Gradiente cálido: amarillo → naranja hacia afuera.
            sk.fill(255, PApplet.lerp(200, 80, t), 40, alpha);
            sk.rect(px, py, tam, tam);
        }

        sk.popStyle();
    }
}
