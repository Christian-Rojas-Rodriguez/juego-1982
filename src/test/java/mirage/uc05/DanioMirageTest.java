package mirage.uc05;

import mirage.model.entidades.Mirage;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;
import mirage.model.fisica.ColisionDetector;
import mirage.model.stats.EstadisticasMirage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UC-05: Enemigo impacta al Mirage. Headless (PApplet sin ventana, solo se usan
 * width/height y aritmética de hitboxes). Verifica que la colisión enemigo↔Mirage
 * resta una vida, activa la invencibilidad temporal y que al llegar a 0 vidas el
 * Mirage deja de estar vivo (condición de game over).
 */
class DanioMirageTest {

    /** PApplet con dimensiones seteadas (campos públicos), sin abrir ventana. */
    private PApplet nuevoApp() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;
        return app;
    }

    @Test
    @DisplayName("Colisión enemigo-Mirage resta una vida y activa invencibilidad")
    void colisionRestaVidaYActivaInvencibilidad() {
        PApplet app = nuevoApp();
        Mirage m = new Mirage(app); // arranca en (200, 500)
        // Enemigo solapado con el Mirage (misma posición → hitboxes se solapan).
        Enemigo e = EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, app, 200, 500);

        ColisionDetector cd = new ColisionDetector(new EstadisticasMirage());
        cd.detectarEnemigoMirage(List.of(e), m);

        assertEquals(2, m.getVidas(), "debe restar exactamente una vida");
        assertTrue(m.isInvencible(), "tras recibir daño debe quedar invencible");
    }

    @Test
    @DisplayName("El Mirage invencible no recibe más daño")
    void invencibleNoRecibeMasDanio() {
        PApplet app = nuevoApp();
        Mirage m = new Mirage(app);
        Enemigo e = EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, app, 200, 500);

        ColisionDetector cd = new ColisionDetector(new EstadisticasMirage());
        cd.detectarEnemigoMirage(List.of(e), m); // primer impacto → invencible
        cd.detectarEnemigoMirage(List.of(e), m); // segundo impacto en el mismo periodo

        assertEquals(2, m.getVidas(), "estando invencible no debe perder otra vida");
    }

    @Test
    @DisplayName("Al llegar a 0 vidas el Mirage deja de estar vivo (game over)")
    void aCeroVidasDejaDeEstarVivo() {
        PApplet app = nuevoApp();
        Mirage m = new Mirage(app); // 3 vidas

        // Tres daños efectivos, esperando que expire la invencibilidad (120 frames)
        // entre cada golpe avanzando el update() del Mirage.
        for (int golpe = 0; golpe < 3; golpe++) {
            for (int f = 0; f < 121; f++) {
                m.update();
            }
            m.recibirDanio(1);
        }

        assertEquals(0, m.getVidas(), "tras 3 daños efectivos debe quedar en 0 vidas");
        assertFalse(m.estaViva(), "con 0 vidas el Mirage no debe estar vivo (game over)");
    }
}
