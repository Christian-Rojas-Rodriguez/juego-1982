// ============================================================
// EstadoGameOver — Estado: partida terminada
// ============================================================
// GRASP : High Cohesion, Information Expert
// Patrón: State (estado concreto)
// ============================================================
// Qué implementar:
//   - alEntrar(): guardar estadísticas, configurar PantallaGameOver
//   - update(): no actualiza física, puede animar la pantalla de GameOver
//   - render(): delegar al renderer con PantallaGameOver activa
//   - onKeyPressed():
//       tecla R → reiniciar (controller.init() + setEstado(new EstadoJugando()))
//       tecla ESC → señalizar al módulo que debe volver al HOME
// ============================================================

package mirage.model.estado;

import mirage.controller.GameController;

public class EstadoGameOver implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        // TODO: controller.getEstadisticas().registrarFinPartida(puntaje)
        // TODO: controller.getEstadisticas().guardar()
        // TODO: configurar PantallaGameOver en el renderer
    }

    @Override
    public void update(GameController controller) {
        // TODO: opcional — animar pantalla de game over
    }

    @Override
    public void render(GameController controller) {
        // TODO: controller.getRenderer().render(...)
    }

    @Override
    public void onKeyPressed(GameController controller, int keyCode) {
        // TODO: tecla R → reiniciar partida
        // TODO: tecla ESC → volver al HOME (señalizar a MirageModulo)
    }
}
