package mirage.uc02;

import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UC-02: Disparar misil. Headless (PApplet sin ventana, solo se usan width/height
 * y aritmética). Verifica la creación de proyectiles, el cooldown entre disparos
 * y el movimiento/desactivación del proyectil al salir de pantalla.
 */
class DisparoMirageTest {

    /** PApplet con dimensiones seteadas (campos públicos), sin abrir ventana. */
    private Mirage nuevoMirage() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;
        return new Mirage(app);
    }

    @Test
    @DisplayName("disparar agrega un proyectil e incrementa disparosTotales")
    void disparoCreaProyectil() {
        Mirage m = nuevoMirage();
        m.disparar();
        assertEquals(1, m.getProyectiles().size());
        assertEquals(1, m.getDisparosTotales());
    }

    @Test
    @DisplayName("el cooldown impide disparar dos veces seguidas, pero permite tras enfriarse")
    void cooldownEntreDisparos() {
        Mirage m = nuevoMirage();
        m.disparar();
        m.disparar(); // bloqueado por cooldown
        assertEquals(1, m.getDisparosTotales());

        // Avanzar suficientes frames para que el cooldown (15) llegue a 0.
        for (int i = 0; i < 16; i++) {
            m.update();
        }
        m.disparar();
        assertEquals(2, m.getDisparosTotales());
    }

    @Test
    @DisplayName("Proyectil.update lo mueve hacia arriba y se desactiva al salir de pantalla")
    void proyectilSubeYSeDesactiva() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;
        Proyectil p = new Proyectil(100, 0, app);

        float y0 = p.getY();
        p.update();
        assertTrue(p.getY() < y0, "el proyectil debe subir (y decrece)");
        assertTrue(p.isActivo());

        // Seguir subiendo hasta salir de pantalla por arriba.
        for (int i = 0; i < 10; i++) {
            p.update();
        }
        assertFalse(p.isActivo(), "fuera de pantalla debe desactivarse");
    }
}
