package mirage.model.fisica;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de ejemplo para HitBox.
 *
 * Demuestra cómo testear una clase real del proyecto:
 *   - Los tests del constructor/getters PASAN hoy (ya está implementado).
 *   - Los tests de colisionaCon() están @Disabled porque el método todavía
 *     es un TODO. Cuando alguien implemente la detección AABB, basta con
 *     borrar la anotación @Disabled para que el test empiece a validar.
 */
class HitBoxTest {

    @Test
    @DisplayName("El constructor y los getters guardan las dimensiones")
    void constructorYGetters() {
        HitBox caja = new HitBox(10f, 20f, 30f, 40f);

        assertEquals(10f, caja.getX());
        assertEquals(20f, caja.getY());
        assertEquals(30f, caja.getAncho());
        assertEquals(40f, caja.getAlto());
    }

    // ── Tests pendientes: activar (quitar @Disabled) al implementar colisionaCon ──

    @Test
    @Disabled("Activar cuando se implemente HitBox.colisionaCon()")
    @DisplayName("Dos hitboxes que se solapan colisionan")
    void hitboxesSolapadasColisionan() {
        HitBox a = new HitBox(0f, 0f, 10f, 10f);
        HitBox b = new HitBox(5f, 5f, 10f, 10f);

        assertTrue(a.colisionaCon(b));
        assertTrue(b.colisionaCon(a)); // la colisión es simétrica
    }

    @Test
    @Disabled("Activar cuando se implemente HitBox.colisionaCon()")
    @DisplayName("Dos hitboxes separadas no colisionan")
    void hitboxesSeparadasNoColisionan() {
        HitBox a = new HitBox(0f, 0f, 10f, 10f);
        HitBox b = new HitBox(100f, 100f, 10f, 10f);

        assertFalse(a.colisionaCon(b));
    }
}
