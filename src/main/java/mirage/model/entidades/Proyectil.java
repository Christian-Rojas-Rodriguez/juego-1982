package mirage.model.entidades;

import processing.core.PApplet;
import processing.core.PImage;
import mirage.model.fisica.HitBox;
import mirage.view.sprites.SpriteLoader;

public class Proyectil extends Nave {

    private static final int ANCHO = 4;
    private static final int ALTO  = 12;
    private static final int SPRITE_ANCHO = 10;  // tamaño de dibujo del sprite
    private static final int SPRITE_ALTO  = 18;

    private boolean activo;
    private final int danio;

    public Proyectil(float x, float y) {
        super(x, y, 8f, 1);
<<<<<<< HEAD
        this.sketch = sketch;
        this.activo = true;
        this.danio = 1;
=======
        this.activo = true;
        this.danio  = 1;
>>>>>>> origin/main
    }

    @Override
    public void update() {
<<<<<<< HEAD
        this.y -= velocidad;
        if (y < -ALTO) {
            desactivar();
        }
    }

    public void render(PApplet sk) {
        //sk.fill(255, 255, 0);  // amarillo pixel-art
        //sk.rect(x - ANCHO/2, y - ALTO/2, ANCHO, ALTO);
    }

    public void desactivar() {
        this.activo = false;
=======
        y -= velocidad;
        if (y < -ALTO) desactivar();
    }

    public void render(PApplet sk) {
        PImage sprite = SpriteLoader.get("bullet.png");
        if (sprite != null) {
            sk.imageMode(PApplet.CENTER);
            sk.image(sprite, x, y, SPRITE_ANCHO, SPRITE_ALTO);
        } else {
            // Respaldo: rectángulo amarillo (modo headless / sin assets).
            sk.fill(255, 255, 0);
            sk.rect(x - ANCHO / 2f, y - ALTO / 2f, ANCHO, ALTO);
        }
    }

    public void desactivar() {
        activo = false;
>>>>>>> origin/main
    }

    @Override
    public HitBox getHitBox() {
<<<<<<< HEAD
        return null; //return new HitBox(x - ANCHO/2, y - ALTO/2, ANCHO, ALTO);
=======
        return new HitBox(x - ANCHO/2f, y - ALTO/2f, ANCHO, ALTO);
>>>>>>> origin/main
    }

    public boolean isActivo() { return activo; }
    public int getDanio()     { return danio; }
}
