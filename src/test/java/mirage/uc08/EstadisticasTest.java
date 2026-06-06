package mirage.uc08;

import mirage.ModuloMirage;
import mirage.model.entidades.Mirage;
import mirage.model.stats.EstadisticasMirage;
import mirage.model.stats.ResumenPartida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * UC-08: Exportar estadísticas al HOME. Headless (PApplet sin ventana, solo se usan
 * width/height). Verifica el cálculo de precisión, el armado del ResumenPartida en
 * exportar() y que ModuloMirage.getEstadisticasGenerales() nunca devuelva null.
 */
class EstadisticasTest {

    /** PApplet con dimensiones seteadas (campos públicos), sin abrir ventana. */
    private PApplet nuevoApplet() {
        PApplet app = new PApplet();
        app.width = 400;
        app.height = 600;
        return app;
    }

    @Test
    @DisplayName("getPrecision: 0 sin disparos; ratio correcto con aciertos")
    void getPrecision() {
        PApplet app = nuevoApplet();
        EstadisticasMirage e = new EstadisticasMirage();
        Mirage m = new Mirage(app);

        assertEquals(0f, e.getPrecision(m));

        m.disparar();                  // 1 disparo total
        e.registrarDisparoAcertado();  // 1 acierto
        assertEquals(1f, e.getPrecision(m), 0.001);
    }

    @Test
    @DisplayName("exportar arma un ResumenPartida con los datos de la partida")
    void exportar() {
        PApplet app = nuevoApplet();
        EstadisticasMirage e = new EstadisticasMirage();
        Mirage m = new Mirage(app);

        e.registrarDerribo("HARRIER", 100);
        e.registrarDerribo("HARRIER", 100);
        m.sumarPuntos(200);

        ResumenPartida r = e.exportar(3, m);
        assertNotNull(r);
        assertEquals(200, r.getPuntajeFinal());
        assertEquals(2, r.getEnemigosDerribados());
        assertEquals(3, r.getVidasRestantes());
    }

    @Test
    @DisplayName("ModuloMirage.getEstadisticasGenerales nunca es null y trae el nombre del modulo")
    void getEstadisticasGenerales() {
        ModuloMirage mod = new ModuloMirage();  // controller null → fallback
        assertNotNull(mod.getEstadisticasGenerales());
        assertEquals("Mirage", mod.getEstadisticasGenerales().getNombreModulo());
    }
}
