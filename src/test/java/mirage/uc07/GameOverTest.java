package mirage.uc07;

import contracts.ContextoJuego;
import contracts.IModuloObserver;
import contracts.ModuloEvento;
import mirage.ModuloMirage;
import mirage.controller.GameController;
import mirage.model.estado.EstadoGameOver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UC-07: al entrar a EstadoGameOver se finaliza el módulo y se notifica
 * FINALIZADO al HOME (cada IModuloObserver recibe el evento).
 *
 * Headless: actualizar(app) no toca graphics; solo avanza el ciclo de vida
 * y crea el GameController interno (que pasa a EN_EJECUCION).
 */
class GameOverTest {

    /** Observer de prueba que registra los tipos de evento recibidos. */
    static class Spy implements IModuloObserver {
        final List<ModuloEvento.Tipo> tipos = new ArrayList<>();
        @Override public void onEventoModulo(ModuloEvento e) { tipos.add(e.getTipo()); }
    }

    @Test
    @DisplayName("Entrar a EstadoGameOver finaliza el modulo y notifica FINALIZADO al HOME")
    void gameOverNotificaFinalizado() throws Exception {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;

        ModuloMirage m = new ModuloMirage();
        Spy spy = new Spy();
        m.agregarObserver(spy);
        m.inicializarContexto(new ContextoJuego("Jugador", 400, 600));
        m.iniciar();
        Thread.sleep(700);   // pasa la carga simulada (INICIANDO -> EN_EJECUCION)
        m.actualizar(app);   // crea el controller interno y entra en EN_EJECUCION

        assertEquals("EN_EJECUCION", m.getEstado().getNombre());

        // Disparar el game over entrando al estado con un controller cuyo modulo es m.
        GameController c = new GameController(app, m);
        c.init();
        c.setEstado(new EstadoGameOver());   // alEntrar -> m.finalizar() -> FINALIZADO

        assertTrue(spy.tipos.contains(ModuloEvento.Tipo.FINALIZADO),
                "El observer del HOME debe recibir FINALIZADO");
        assertEquals("FINALIZADO", m.getEstado().getNombre());
    }
}
