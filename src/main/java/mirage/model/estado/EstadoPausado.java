// ============================================================
// EstadoPausado — Estado: juego pausado, solo renderiza
// ============================================================
// GRASP : High Cohesion
// Patrón: State (estado concreto)
// ============================================================
// Qué implementar:
//   - update(): NO actualizar nada (es la razón de existir de este estado)
//   - render(): mostrar la pantalla de juego "congelada" + texto "PAUSADO"
//   - onKeyPressed(): tecla P → setEstado(new EstadoJugando())
// ============================================================

package mirage.model.estado;

import mirage.controller.GameController;

public class EstadoPausado implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        // TODO: mostrar indicador visual de pausa
    }

    @Override
    public void update(GameController controller) {
        // No actualizar nada — el juego está pausado
    }

    @Override
    public void render(GameController controller) {
        // TODO: renderizar el estado actual del juego (congelado)
        // TODO: dibujar encima texto "PAUSADO" centrado en pantalla
    }

    @Override
    public void onKeyPressed(GameController controller, int keyCode) {
        // TODO: si keyCode == 'p' o 'P' → controller.setEstado(new EstadoJugando())
    }
}
