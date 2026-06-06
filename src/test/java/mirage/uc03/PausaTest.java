package mirage.uc03;

import mirage.ModuloMirage;
import mirage.controller.GameController;
import mirage.controller.commands.MoverDerechaCmd;
import mirage.model.entidades.Mirage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UC-03: Pausar / Reanudar.
 *
 * Como no hay getter del estado activo, la verificación es BEHAVIORAL:
 * con flag de movimiento activo, en pausa el Mirage NO se mueve (update() = no-op)
 * y al reanudar SÍ se mueve. Headless: PApplet sin ventana con width/height fijados.
 */
class PausaTest {

    @Test
    @DisplayName("'P' pausa (no actualiza) y 'P' de nuevo reanuda (vuelve a actualizar)")
    void pausarYReanudar() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;

        GameController c = new GameController(app, new ModuloMirage());
        c.init();                               // estado inicial: EstadoJugando

        Mirage m = c.getMirage();
        new MoverDerechaCmd().ejecutar(m);      // activa flag de movimiento a la derecha
        float x0 = m.getX();

        c.onKeyPressed('p', 80);                // PAUSA
        c.update();                             // EstadoPausado.update() = no-op
        assertEquals(x0, m.getX(), 0.001, "en pausa el Mirage no debe moverse");

        c.onKeyPressed('p', 80);                // REANUDA
        c.update();                             // EstadoJugando.update() mueve
        assertTrue(m.getX() > x0, "al reanudar el Mirage debe volver a moverse");
    }
}
