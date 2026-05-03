// ============================================================
// HitBox — Caja de colisión rectangular (AABB)
// ============================================================
// GRASP : Information Expert (sabe si colisiona con otra HitBox)
// ============================================================
// Qué implementar:
//   - colisionaCon(HitBox otro): detectar solapamiento AABB
//     Algoritmo: dos rectángulos NO colisionan si uno está
//     completamente a la izquierda, derecha, arriba o abajo del otro.
//     Negar esa condición da la colisión.
//   - moverA(x, y): actualizar posición (llamado en cada frame)
// ============================================================

package mirage.model.fisica;

public class HitBox {

    // --- Atributos ---
    private float x;
    private float y;
    private final float ancho;
    private final float alto;

    // --- Constructor ---
    public HitBox(float x, float y, float ancho, float alto) {
        // TODO: asignar todos los parámetros
    }

    // --- Detección AABB ---
    public boolean colisionaCon(HitBox otro) {
        // TODO:
        // return !(this.x + this.ancho < otro.x ||
        //          otro.x  + otro.ancho < this.x  ||
        //          this.y + this.alto  < otro.y ||
        //          otro.y  + otro.alto  < this.y);
        return false;
    }

    // --- Actualizar posición cada frame ---
    public void moverA(float x, float y) {
        // TODO: this.x = x; this.y = y;
    }

    // --- Getters ---
    public float getX()     { return x; }
    public float getY()     { return y; }
    public float getAncho() { return ancho; }
    public float getAlto()  { return alto; }
}
