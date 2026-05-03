// ============================================================
// Nave — Clase abstracta base para todas las entidades volantes
// ============================================================
// GRASP : Information Expert, Protected Variations
// Patrón: Template Method (subclases implementan update y render)
// ============================================================
// Qué implementar:
//   - Constructor: inicializar x, y, velocidad, vida
//   - estaViva(): retorna vida > 0
//   - recibirDanio(): descuenta vida
//   - getHitBox(): construir HitBox centrada en (x, y)
//   - update() y render() abstractos — cada subclase define su comportamiento
// Nota: esta clase NO importa Processing. La vista recibe sketch como parámetro.
// ============================================================

package mirage.model.entidades;

import mirage.model.fisica.HitBox;

public abstract class Nave {

    // --- Atributos ---
    protected float x;
    protected float y;
    protected float velocidad;
    protected int vida;

    // --- Constructor ---
    public Nave(float x, float y, float velocidad, int vida) {
        // TODO: asignar parámetros
    }

    // --- Comportamiento abstracto (Template Method) ---
    public abstract void update();

    // --- Estado ---
    public boolean estaViva() {
        // TODO: return vida > 0
        return false;
    }

    public void recibirDanio(int danio) {
        // TODO: vida -= danio
    }

    // --- HitBox centrada en la posición actual ---
    public abstract HitBox getHitBox();

    // --- Getters ---
    public float getX() { return x; }
    public float getY() { return y; }
    public int getVida()  { return vida; }
}
