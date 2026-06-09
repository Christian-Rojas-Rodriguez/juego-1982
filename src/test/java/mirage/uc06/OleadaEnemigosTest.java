package mirage.uc06;

import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;
import mirage.model.entidades.enemigos.HarrierEnemigo;
import mirage.model.niveles.EnemySpawner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UC-06: Oleada de enemigos se activa.
 *
 * Verifica la fábrica de enemigos, la generación de oleadas por el EnemySpawner
 * al alcanzar el intervalo de frames, y el descenso del HarrierEnemigo al
 * actualizarse. Headless: usa un PApplet sin ventana con width/height fijados.
 * PApplet.random(...) funciona sin ventana.
 */
class OleadaEnemigosTest {

    /** PApplet sin ventana; solo aporta dimensiones y random(). */
    private PApplet nuevaApp() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;
        return app;
    }

    @Test
    @DisplayName("EnemyFactory crea un HarrierEnemigo")
    void enemyFactoryCreaHarrier() {
        PApplet app = nuevaApp();

        Enemigo e = EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, app, 100, -20);

        assertNotNull(e);
        assertInstanceOf(HarrierEnemigo.class, e);
        assertTrue("HARRIER".equals(e.getTipo()));
    }

    @Test
    @DisplayName("EnemySpawner genera tamanoOleada enemigos al alcanzar el intervalo")
    void spawnerGeneraOleadaAlIntervalo() {
        PApplet app = nuevaApp();
        EnemySpawner sp = new EnemySpawner(app, 2, 3);

        sp.update(); // frameCounter = 1, aún no spawnea
        assertTrue(sp.getEnemigosNuevos().isEmpty());

        sp.update(); // frameCounter = 2 >= 2, spawnea la oleada
        List<Enemigo> nuevos = sp.getEnemigosNuevos();
        assertEquals(3, nuevos.size());

        // El buffer se limpió tras la consulta anterior.
        assertTrue(sp.getEnemigosNuevos().isEmpty());
    }

    @Test
    @DisplayName("HarrierEnemigo desciende al actualizarse")
    void harrierDesciendeAlActualizarse() {
        PApplet app = nuevaApp();
        Enemigo e = EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, app, 200, 0);

        float y0 = e.getY();
        e.update();

        assertTrue(e.getY() > y0);
    }
}
