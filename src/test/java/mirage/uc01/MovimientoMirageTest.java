package mirage.uc01;

import mirage.controller.InputHandler;
import mirage.controller.commands.MoverDerechaCmd;
import mirage.model.entidades.Mirage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UC-01: Mover el Mirage.
 *
 * Verifica el flujo Command → flag de movimiento → update() aplica el desplazamiento
 * con clamp dentro de los límites de la pantalla. Headless: usa un PApplet sin ventana
 * al que se le fijan width/height (campos públicos) para dar dimensiones.
 */
class MovimientoMirageTest {

    /** PApplet sin ventana; solo aporta dimensiones para el constrain(). */
    private PApplet nuevaApp() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;
        return app;
    }

    @Test
    @DisplayName("MoverDerechaCmd activa el flag y update() mueve el Mirage a la derecha con clamp")
    void moverDerechaMueveConClamp() {
        PApplet app = nuevaApp();
        Mirage m = new Mirage(app);

        float x0 = m.getX();
        new MoverDerechaCmd().ejecutar(m);
        m.update();

        assertTrue(m.getX() > x0, "el Mirage debe moverse a la derecha");
        assertTrue(m.getX() <= app.width, "el Mirage no debe salir del borde derecho");
    }

    @Test
    @DisplayName("deshacer detiene el movimiento")
    void deshacerDetieneMovimiento() {
        PApplet app = nuevaApp();
        Mirage m = new Mirage(app);

        new MoverDerechaCmd().deshacer(m);
        float x1 = m.getX();
        m.update();

        assertEquals(x1, m.getX(), "sin flag activo, update() no debe cambiar la posición");
    }

    @Test
    @DisplayName("InputHandler.registrarComando + onKeyPressed mueve el Mirage")
    void inputHandlerMueve() {
        PApplet app = nuevaApp();
        Mirage m = new Mirage(app);

        InputHandler input = new InputHandler();
        input.registrarComando(PApplet.RIGHT, new MoverDerechaCmd());

        float x0 = m.getX();
        input.onKeyPressed(PApplet.RIGHT, ' ', m);
        m.update();

        assertTrue(m.getX() > x0, "tras onKeyPressed(RIGHT) y update() el Mirage debe avanzar");
    }
}
