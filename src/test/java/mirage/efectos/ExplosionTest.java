package mirage.efectos;

import mirage.ModuloMirage;
import mirage.controller.GameController;
import mirage.model.efectos.Explosion;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;
import mirage.model.estado.EstadoJugando;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Efecto de explosión: ciclo de vida.
 *
 * Verifica que la explosión arranca activa y se marca como terminada tras
 * agotar su duración en frames. No prueba el dibujo (requiere ventana);
 * solo la lógica de vida, que es lo que el resto del juego consulta para
 * descartar el efecto. Headless: no necesita PApplet.
 */
class ExplosionTest {

    @Test
    @DisplayName("La explosión arranca sin estar terminada")
    void arrancaActiva() {
        Explosion ex = new Explosion(100, 200);
        assertFalse(ex.terminada());
    }

    @Test
    @DisplayName("La explosión termina tras agotar su duración")
    void terminaTrasSuDuracion() {
        Explosion ex = new Explosion(100, 200);

        // Avanzar suficientes frames para superar cualquier duración razonable.
        for (int i = 0; i < 60 && !ex.terminada(); i++) {
            ex.update();
        }

        assertTrue(ex.terminada());
    }

    @Test
    @DisplayName("EstadoJugando crea una explosión al destruir un enemigo")
    void spawnAlDestruirEnemigo() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;

        // GameController headless: init() solo arma el modelo, no toca graphics.
        GameController gc = new GameController(app, new ModuloMirage());
        gc.init();

        // Enemigo ya destruido (vida 0) colocado lejos del Mirage.
        Enemigo muerto = EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, app, 50, 50);
        muerto.recibirDanio(1);                 // HarrierEnemigo tiene 1 de vida → muere
        assertFalse(muerto.estaViva());
        gc.getEnemigos().add(muerto);

        new EstadoJugando().update(gc);          // no llama render(); es headless-safe

        assertTrue(gc.getEfectos().size() >= 1, "debe haberse creado al menos una explosión");
    }
}
