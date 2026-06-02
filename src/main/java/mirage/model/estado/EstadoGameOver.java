package mirage.model.estado;

import home.TipoEvento;
import mirage.controller.GameController;

/**
 * Estado: partida terminada (vidas del Mirage = 0).
 *
 * Al entrar: registra fin de partida en estadísticas, notifica al HOME
 * que el módulo finalizó, y configura la pantalla de Game Over en el renderer.
 *
 * Durante update(): no hace nada — espera input del jugador.
 * onKeyPressed(): R reinicia, ESC vuelve al HOME.
 *
 * Patrón: State
 */
public class EstadoGameOver implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        // TODO: int puntaje = controller.getMirage().getPuntuacion()
        // TODO: controller.getEstadisticas().registrarFinPartida(puntaje)
        // TODO: controller.getEstadisticas().guardar()
        // TODO: controller.getMirageModulo().notificar(TipoEvento.FINALIZADO)
        // TODO: PantallaGameOver pantalla = new PantallaGameOver(puntaje, enemigosDerribados)
        // TODO: controller.getRenderer().setPantalla(pantalla)
    }

    @Override
    public void update(GameController controller) {
        // No actualizar — espera input del jugador para reiniciar o salir
    }

    @Override
    public void render(GameController controller) {
        // TODO: controller.getRenderer().render(controller.getSketch(),
        //           controller.getMirage(), controller.getEnemigos())
    }

    @Override
    public void onKeyPressed(GameController controller, char key, int keyCode) {
        // TODO: si key == 'r' o key == 'R' → controller.getMirageModulo().reset()
        // TODO: si keyCode == ESC → controller.getMirageModulo().finalizar()
    }
}
