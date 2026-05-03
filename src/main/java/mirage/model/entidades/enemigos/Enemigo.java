// ============================================================
// Enemigo — Clase abstracta base para todos los enemigos
// ============================================================
// GRASP : Polymorphism, Protected Variations
// Patrón: Template Method — update() es final, moverIA() es el hook
// ============================================================
// Qué implementar:
//   - update() final: llama a moverIA() — NO sobreescribir en subclases
//   - moverIA() abstracto: cada subclase define su comportamiento de IA
//   - render() abstracto: cada subclase dibuja su sprite
//   - getHitBox(): hitbox del tamaño apropiado para el enemigo
// ============================================================

package mirage.model.entidades.enemigos;

import processing.core.PApplet;
import mirage.model.entidades.Nave;
import mirage.model.fisica.HitBox;

public abstract class Enemigo extends Nave {

    // --- Atributos ---
    protected int puntos;
    protected final PApplet sketch;

    // --- Constructor ---
    public Enemigo(PApplet sketch, float x, float y, float velocidad, int vida, int puntos) {
        super(x, y, velocidad, vida);
        this.sketch = sketch;
        // TODO: this.puntos = puntos
    }

    // Template Method: ciclo de vida fijo
    @Override
    public final void update() {
        moverIA();
    }

    // Hook: cada subclase implementa su lógica de movimiento
    protected abstract void moverIA();

    // Cada subclase dibuja su propio sprite
    public abstract void render(PApplet sk);

    @Override
    public abstract HitBox getHitBox();

    // --- Getter ---
    public int getPuntos() { return puntos; }
}
