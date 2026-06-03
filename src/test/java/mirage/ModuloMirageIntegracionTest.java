package mirage;

import contracts.ContextoJuego;
import contracts.EstadoInvalidoException;
import contracts.IModuloObserver;
import contracts.ModuloEvento;
import lobby.GestorModulos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test de integración: verifica que ModuloMirage cumple el contrato real del HOME
 * (contracts.ModuloJuego) y que el lobby puede registrarlo y manejarlo.
 *
 * Es headless: ejercita la máquina de estados, observers y estadísticas sin abrir
 * ventana. actualizar(app) no usa graphics (solo avanza el ciclo de vida), por eso
 * se le puede pasar un PApplet sin inicializar. dibujar() (que sí usa graphics) se
 * verifica manualmente con HomeRunner.
 */
class ModuloMirageIntegracionTest {

    /** Observer de prueba que registra los eventos recibidos. */
    private static class ObserverSpy implements IModuloObserver {
        final List<ModuloEvento.Tipo> recibidos = new ArrayList<>();
        @Override public void onEventoModulo(ModuloEvento evento) {
            recibidos.add(evento.getTipo());
        }
    }

    private final PApplet appDummy = new PApplet(); // no se abre ventana; actualizar() no usa graphics

    @Test
    @DisplayName("Identidad del módulo conforme al contrato")
    void identidad() {
        ModuloMirage m = new ModuloMirage();
        assertEquals("Mirage", m.getNombreModulo());
        assertEquals("Dassault Mirage III", m.getNombreAvion());
        assertNotNull(m.getDescripcion());
        assertFalse(m.getDescripcion().isEmpty());
    }

    @Test
    @DisplayName("Estado inicial es NO_INICIADO")
    void estadoInicial() {
        assertEquals("NO_INICIADO", new ModuloMirage().getEstado().getNombre());
    }

    @Test
    @DisplayName("Ciclo de vida completo: iniciar → ejecutar → pausar → reanudar → finalizar")
    void cicloDeVida() throws Exception {
        ModuloMirage m = new ModuloMirage();
        ObserverSpy spy = new ObserverSpy();
        m.agregarObserver(spy);
        m.inicializarContexto(new ContextoJuego("Jugador", 800, 600));

        m.iniciar();
        assertEquals("INICIANDO", m.getEstado().getNombre());
        assertTrue(spy.recibidos.contains(ModuloEvento.Tipo.INICIADO));

        // Esperar a que termine la carga simulada y avanzar el loop.
        Thread.sleep(700);
        m.actualizar(appDummy);
        assertEquals("EN_EJECUCION", m.getEstado().getNombre());

        m.pausar();
        assertEquals("PAUSADO", m.getEstado().getNombre());
        assertTrue(spy.recibidos.contains(ModuloEvento.Tipo.PAUSADO));

        m.reanudar();
        assertEquals("EN_EJECUCION", m.getEstado().getNombre());
        assertTrue(spy.recibidos.contains(ModuloEvento.Tipo.REANUDADO));

        m.finalizar();
        assertEquals("FINALIZADO", m.getEstado().getNombre());
        assertTrue(spy.recibidos.contains(ModuloEvento.Tipo.FINALIZADO));
    }

    @Test
    @DisplayName("Transición inválida lanza EstadoInvalidoException")
    void transicionInvalida() {
        ModuloMirage m = new ModuloMirage();
        // No se puede pausar sin haber iniciado.
        assertThrows(EstadoInvalidoException.class, m::pausar);
    }

    @Test
    @DisplayName("getEstadisticasGenerales nunca es null y trae el nombre del módulo")
    void estadisticasNoNull() throws Exception {
        ModuloMirage m = new ModuloMirage();
        m.inicializarContexto(new ContextoJuego("Jugador", 800, 600));
        m.iniciar();
        assertNotNull(m.getEstadisticasGenerales());
        assertEquals("Mirage", m.getEstadisticasGenerales().getNombreModulo());
    }

    @Test
    @DisplayName("reset vuelve a NO_INICIADO y limpia observers")
    void resetLimpia() throws Exception {
        ModuloMirage m = new ModuloMirage();
        ObserverSpy spy = new ObserverSpy();
        m.agregarObserver(spy);
        m.iniciar();
        m.reset();
        assertEquals("NO_INICIADO", m.getEstado().getNombre());

        // Tras reset, el observer fue removido: no debe recibir nuevos eventos.
        int antes = spy.recibidos.size();
        m.iniciar();
        assertEquals(antes, spy.recibidos.size());
    }

    @Test
    @DisplayName("El lobby (GestorModulos) registra y encuentra el módulo por nombre")
    void registroEnLobby() throws Exception {
        GestorModulos gestor = new GestorModulos();
        gestor.agregarModulo(new ModuloMirage());
        assertEquals(1, gestor.listarModulos().size());
        // buscarModulo es case-insensitive.
        assertNotNull(gestor.buscarModulo("mirage"));
        assertEquals("Mirage", gestor.buscarModulo("MIRAGE").getNombreModulo());
    }
}
