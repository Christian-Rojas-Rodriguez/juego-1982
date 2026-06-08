package mirage.model.entidades;

import processing.core.PApplet;
import processing.core.PImage;
import mirage.model.fisica.HitBox;
import mirage.view.sprites.SpriteLoader;

/** Misil disparado por el Mirage: sube hasta salir de pantalla y se desactiva. */
public class Proyectil extends Nave {

    private static final int ANCHO = 4;
    private static final int ALTO  = 12;
    private static final int SPRITE_ANCHO = 10;  // tamaño de dibujo del sprite
    private static final int SPRITE_ALTO  = 18;

    private boolean activo;
    private final int danio;
    private final PApplet sketch;

    public Proyectil(float x, float y, PApplet sketch) {
        super(x, y, 8f, 1);
        this.sketch = sketch;
        this.activo = true;
        this.danio  = 1;
    }

    @Override
    public void update() {
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
    }

    @Override
    public HitBox getHitBox() {
        return new HitBox(x - ANCHO/2f, y - ALTO/2f, ANCHO, ALTO);
    }

    public boolean isActivo() { return activo; }
    public int getDanio()     { return danio; }
}
