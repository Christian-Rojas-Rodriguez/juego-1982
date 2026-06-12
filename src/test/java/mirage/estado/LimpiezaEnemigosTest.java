package mirage.estado;

import mirage.ModuloMirage;
import mirage.controller.GameController;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;
import mirage.model.estado.EstadoJugando;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EstadoJugando: limpieza de enemigos que salen por debajo de la pantalla.
 *
 * Verifica que un enemigo vivo cuya posición Y queda por debajo del borde
 * inferior se descarta en el siguiente update() (evita la acumulación
 * indefinida de entidades fuera de vista). Headless: GameController.init()
 * y EstadoJugando.update() no tocan graphics.
 */
class LimpiezaEnemigosTest {

    @Test
    @DisplayName("Un enemigo que sale por debajo de la pantalla se elimina")
    void enemigoFueraDePantallaSeElimina() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;

        GameController gc = new GameController(app, new ModuloMirage());
        gc.init();

        // Enemigo vivo pero muy por debajo del borde inferior (height + margen).
        Enemigo abajo = EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, app, 200, app.height + 200);
        assertTrue(abajo.estaViva());
        gc.getEnemigos().add(abajo);

        new EstadoJugando().update(gc);

        assertFalse(gc.getEnemigos().contains(abajo),
                "el enemigo fuera de pantalla debe haberse eliminado");
    }
}
