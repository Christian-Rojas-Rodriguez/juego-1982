// ============================================================
// Proyectil — Misil disparado por el Mirage o por enemigos
// ============================================================
// GRASP : Information Expert
// ============================================================
// Qué implementar:
//   - update(): mover hacia arriba (y -= velocidad)
//               si sale de pantalla → desactivar()
//   - render(sketch): dibujar el proyectil (rectángulo o imagen)
//   - desactivar(): activo = false (el Controller lo elimina de la lista)
//   - getHitBox(): hitbox pequeña centrada en (x, y)
// ============================================================

package mirage.model.entidades;

import processing.core.PApplet;
import mirage.model.fisica.HitBox;

public class Proyectil extends Nave {

    // --- Constantes ---
    private static final int ANCHO = 4;
    private static final int ALTO  = 12;

    // --- Atributos ---
    private boolean activo;
    private final int danio;
    private final PApplet sketch;

    // --- Constructor ---
    public Proyectil(float x, float y, PApplet sketch) {
        super(x, y, 8f, 1);
        this.sketch = sketch;
        activo = true; 
        danio = 1;
    }

    @Override
    public void update() {
        // TODO: y -= velocidad
        // TODO: si y < -ALTO → desactivar()
    }

    public void render(PApplet sk) {
        // TODO: sk.fill(255, 255, 0)  // amarillo pixel-art
        // TODO: sk.rect(x - ANCHO/2, y - ALTO/2, ANCHO, ALTO)
    }

    public void desactivar() {
        activo = false;
    }

    @Override
    public HitBox getHitBox() {
        return new HitBox(x - ANCHO/2f, y - ALTO/2f, ANCHO, ALTO);
    }

    // --- Getters ---
    public boolean isActivo() { return activo; }
    public int getDanio()     { return danio; }
}
